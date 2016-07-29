package org.uulib.grmd.task

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.*

/**
 * Tests {@linkplain MarkdownCompile}.
 * @author Rowan Lonsdale
 */
class MarkdownCompileSpec extends Specification {
	
	@Rule TemporaryFolder projectFolder = new TemporaryFolder()
	
	def setup() {
		File buildFile = projectFolder.newFile('build.gradle')
		buildFile.text =
"""
//import ${MarkdownCompile.class.name}
plugins {
	id 'org.uulib.grmd.grmd-base'
}

defaultTasks('testCompileMarkdown')

task(testCompileMarkdown, type: MarkdownCompile) {
	source 'myMarkdown'
	destinationDir = file('myHTML')
}
"""
	}
	
	def "The MarkdownCompile task produces HTML files"() {
		setup:
		String fileName = 'document'
		File markdownFile = new File(projectFolder.newFolder('myMarkdown'), "${fileName}.md")
		markdownFile.text =
"""
# Test Document
This is a test document to check the markdown compiler is working:
 * An HTML file with the same name should be created
 * That HTML file should contain HTML compiled by [Pegdown](http://pegdown.org)
"""
		
		when:
		BuildResult result = GradleRunner.create()
				.withProjectDir(projectFolder.root)
				.withPluginClasspath()
				.withDebug(true)
				.build()
		BuildTask task = result.task(':testCompileMarkdown')
		File html = new File(projectFolder.root, "myHTML")
		
		then:
		assert task!=null : "MarkdownCompile task was not executed. Tasks run: $result.tasks"
		TaskOutcome.SUCCESS == task.outcome
		assert html.directory : "${projectFolder.root.listFiles()}"
		assert new File(html, "${fileName}.html").file : "${html.listFiles()}"
	}

}
