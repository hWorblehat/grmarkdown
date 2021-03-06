/*
 * Copyright (c) 2016 Rowan Lonsdale.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.uulib.gradle.markdown.plugin

import java.nio.file.Path
import java.nio.file.Paths

import org.junit.ClassRule
import org.junit.rules.TemporaryFolder
import org.uulib.gradle.markdown.model.HtmlBinary;
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome;

import spock.lang.*

import static org.uulib.gradle.markdown.TestUtils.*

class MarkdownModelPluginSpec extends Specification {
	
	@Shared String buildScriptText = 
"""
plugins {
	id '${pluginIdBase}.markdown-lang'
}

model {

	components {
		instructions(DocumentationSpec)
		
		warnings(GeneralComponentSpec) {
			sources {
				minorWarnings(MarkdownSourceSet)
				severeWarnings(MarkdownSourceSet)
			}
			binaries {
				warningPages(HtmlBinary)
			}
		}
	}

	tasks {
		printTaskDirs(Task) {
			Task other = \$.tasks.compileWarningsWarningPagesSevereWarnings
			java.nio.file.Path projectPath = project.projectDir.toPath()
			doLast {
				println 'Input: ' + other.source.srcDirs.collect {projectPath.relativize(it.toPath())}
				println 'Output: ' + projectPath.relativize(other.destinationDir.toPath())
			}
		}
	}

}
"""
	
	@Shared @ClassRule TemporaryFolder projectFolder = new TemporaryFolder()
	
	def setupSpec() {
		File buildFile = projectFolder.newFile('build.gradle')
		buildFile.text = buildScriptText
	}
	
	def "Model can be obtained"() {
		
		when:
		BuildResult result = getGradleRunner(projectFolder.root, 'model', '--format', 'short').build()
		
		then:
		noExceptionThrown()
		println result.output
	}
	
	def "Components are correct"() {
		
		when:
		BuildResult result = getGradleRunner(projectFolder.root, 'components').build()
		
		then:
		//println result.output
		result.output =~ sourceSetComponent('instructions', 'markdown')
		result.output =~ soleBinaryComponent('instructions', 'htmlDocument')
		result.output =~ sourceSetComponent('warnings', 'minorWarnings')
		result.output =~ sourceSetComponent('warnings', 'severeWarnings')
		result.output =~ soleBinaryComponent('warnings', 'warningPages')
	}
	
	def "Tasks were run"() {
		
		when:
		BuildResult result = getGradleRunner(projectFolder.root, '-m', 'assemble').build()
		
		then:
		//println result.output
		[
			'compileInstructionsHtmlDocumentMarkdown',
			'instructionsHtmlDocument',
			'compileWarningsWarningPagesMinorWarnings',
			'compileWarningsWarningPagesSevereWarnings',
			'warningsWarningPages'
		].each { String task ->
			BuildTask t = result.task(":$task")
			assert t!=null
			assert TaskOutcome.SKIPPED == t.outcome
		}
	}
	
	def "Task has the correct destination dir"() {
		setup:
		Path srcFile = Paths.get('src','warnings','severeWarnings')
		Path dstFile = Paths.get('build','warnings','warningPages')
		
		when:
		BuildResult result = getGradleRunner(projectFolder.root, 'printTaskDirs').build()
		
		then:
		TaskOutcome.SUCCESS == result.task(':printTaskDirs').outcome
		result.output.contains "Input: [$srcFile]"
		result.output.contains "Output: $dstFile"
	}
	
	String soleBinaryComponent(String component, String binary) {
		/Binaries\s+${HtmlBinary.class.simpleName} '${component}:${binary}'/
	}
	
	String sourceSetComponent(String component, String name) {
		String psep = File.separator
		/Markdown source '${component}:${name}'\s+srcDir: \Qsrc${psep}${component}${psep}${name}\E\s+includes: \Q**\/*.markdown, **\/*.md\E/
	}

}
