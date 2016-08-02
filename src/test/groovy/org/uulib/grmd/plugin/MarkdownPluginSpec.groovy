package org.uulib.grmd.plugin

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder

import spock.lang.*

import static org.uulib.grmd.TestUtils.*

class MarkdownPluginSpec extends Specification {
	
	@Rule TemporaryFolder projectFolder = new TemporaryFolder()
	
	def setup() {
		File buildFile = projectFolder.newFile('build.gradle')
		buildFile.text =
"""
plugins {
	id 'org.uulib.grmd.markdown'
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
