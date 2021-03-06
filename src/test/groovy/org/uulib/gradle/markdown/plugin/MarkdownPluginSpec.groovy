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

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.*

import static org.uulib.gradle.markdown.TestUtils.*

class MarkdownPluginSpec extends Specification {
	
	@Rule TemporaryFolder projectFolder = new TemporaryFolder()
	
	def setup() {
		File buildFile = projectFolder.newFile('build.gradle')
		buildFile.text =
"""
plugins {
	id '${pluginIdBase}.markdown'
}
"""
	}
	
	def "The markdown plugin creates the 'compileMarkdown' task"() {
		when:
		BuildResult result = getGradleRunner(projectFolder.root, 'tasks').build()
		
		then:
		BuildTask taskResult = result.task(":tasks")
		taskResult!=null
		taskResult.outcome == TaskOutcome.SUCCESS
		result.output.contains('compileMarkdown - Compiles the docs markdown source.')
	}
	
	def "The 'compileMarkdown' task is a dependency of the 'assemble' task"() {
		when:
		BuildResult result = getGradleRunner(projectFolder.root).withArguments('-m', 'assemble').build()
		
		then:
		BuildTask taskResult = result.task(":compileMarkdown")
		taskResult!=null
		taskResult.outcome == TaskOutcome.SKIPPED
	}
	
	def "The 'compileMarkdown' task compiles markdown"() {
		setup:
		File srcFolder = projectFolder.newFolder('src', 'docs', 'markdown')
		File srcFile = new File(srcFolder, 'info.md')
		srcFile.text = sampleMarkdownText
		
		when:
		BuildResult result = getGradleRunner(projectFolder.root, 'build').build()
		
		then:
		//println result.output
		BuildTask taskResult = result.task(":compileMarkdown")
		taskResult!=null
		taskResult.outcome == TaskOutcome.SUCCESS
		File htmlDir = new File(projectFolder.root, 'build/docs/html')
		htmlDir.directory
		assert new File(htmlDir, 'info.html').file : "${htmlDir.listFiles()}"
	}

}
