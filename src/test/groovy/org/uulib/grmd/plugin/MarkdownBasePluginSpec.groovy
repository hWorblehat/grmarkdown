package org.uulib.grmd.plugin

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.model.ModelMap;
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask;
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder
import org.uulib.grmd.task.MarkdownCompile

import spock.lang.*

import static org.uulib.grmd.TestUtils.*

class MarkdownBasePluginSpec extends Specification {
	
	@Rule TemporaryFolder projectFolder = new TemporaryFolder()
	
	def "Applying the base plugin sets the description for MarkdownCompile tasks"(String sourceName,
			String expectedDescription) {
		setup:
		File buildFile = projectFolder.newFile('build.gradle')
		buildFile.text = 
"""
plugins {
	id '${pluginIdBase}.markdown-base'
}

task('myTask', type: MarkdownCompile) {
	sourceName = '${sourceName}'
}
"""
		
		when:
		BuildResult result = getGradleRunner(projectFolder.root, 'tasks').build()
		
		then:
		BuildTask taskResult = result.task(":tasks")
		taskResult!=null
		taskResult.outcome == TaskOutcome.SUCCESS
		result.output.contains("myTask - ${expectedDescription}")
		
		where:
		sourceName | expectedDescription
		''         | 'Compiles markdown source.'
		'idiotic'  | 'Compiles the idiotic markdown source.'
	}
	
	

}
