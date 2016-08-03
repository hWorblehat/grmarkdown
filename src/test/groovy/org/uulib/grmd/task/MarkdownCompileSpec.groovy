package org.uulib.grmd.task

import java.nio.file.Path
import java.nio.file.Paths

import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.BuildTask
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import org.uulib.grmd.TestUtils
import org.uulib.grmd.plugin.MarkdownBasePlugin

import spock.lang.*

import static org.uulib.grmd.TestUtils.*

/**
 * Tests {@linkplain MarkdownCompile}.
 * @author Rowan Lonsdale
 */
class MarkdownCompileSpec extends Specification {
	
	private static final String DEFAULT_MARKDOWN_FOLDER = 'myMarkdown'
	private static final String DEFAULT_HTML_FOLDER = 'myHTML'
	private static final String DEFAULT_TASK_NAME = 'testCOmpileMarkdown'
	
	@Rule TemporaryFolder projectFolder = new TemporaryFolder()
	File markdownFolder, htmlFolder
	
	def setup() {
		markdownFolder = projectFolder.newFolder(DEFAULT_MARKDOWN_FOLDER)
		htmlFolder = projectFolder.newFolder(DEFAULT_HTML_FOLDER)
	}
	
	def "A MarkdownCompile task has the expected properties"() {
		setup:
		Project project = ProjectBuilder.builder().build()
		String taskName = 'compileSomeMarkdown'
		
		when:
		MarkdownCompile task = project.task([type: MarkdownCompile], taskName)
		
		then:
		task != null
		task.name==taskName
		task.group==MarkdownBasePlugin.MARKDOWN_TASK_GROUP
	}
	
	def "The MarkdownCompile task produces HTML files"() {
		setup:
		generateBuildscript()
		String fileName = 'document'
		File markdownFile = new File(markdownFolder, "${fileName}.md")
		markdownFile.text = sampleMarkdownText
		
		when:
		BuildResult result = runBuild()
		
		then:
		checkBuildResult(result)
		checkHtmlFiles([fileName])
	}
	
	def "The MarkdownCompile task runs incrementally"() {
		setup:
		generateBuildscript()
		String fileName = 'document'
		String fileName2 = 'grr/document2'
		File markdownFile = new File(markdownFolder, "${fileName}.md")
		File markdownFile2 = new File(markdownFolder, "${fileName2}.markdown")
		markdownFile.text = sampleMarkdownText
		
		File htmlFile = new File(htmlFolder, "${fileName}.html")
		
		when:
		BuildResult result = runBuild()
		
		then:
		//println result.output
		checkBuildResult(result)
		checkHtmlFiles([fileName], [fileName2])
		
		when:
		//println htmlFile.text
		long modifiedTime = htmlFile.lastModified()
		markdownFile2.parentFile.mkdirs()
		markdownFile2.text = sampleMarkdownText
		Thread.sleep(4) // Sleep to ensure file modification timestamps will be different
		result = runBuild()
		
		then:
		//println result.output
		checkBuildResult(result)
		checkHtmlFiles([fileName, fileName2])
		modifiedTime == htmlFile.lastModified()
		
		when:
		markdownFile2.delete()
		result = runBuild()
		
		then:
		//println result.output
		checkBuildResult(result)
		checkHtmlFiles([fileName], [fileName2])
		modifiedTime == htmlFile.lastModified()
	}
	
	private void checkBuildResult(BuildResult result,
			Map<String, TaskOutcome> expected = [(DEFAULT_TASK_NAME): TaskOutcome.SUCCESS]) {
		expected.each { String taskName, TaskOutcome expectedOutcome ->
			BuildTask task = result.task(":$taskName")
			assert task!=null : "Expected task '$taskName' was not executed. Tasks run: $result.tasks"
			assert expectedOutcome == task.outcome
		}
	}
			
	private void checkHtmlFiles(List<String> shouldExist, List<String> shouldNotExist = [],
			List<String> outputFolderPath = [DEFAULT_HTML_FOLDER]) {
		File outputFolder = projectFolder.root
		String names = ''
		for(String name: outputFolderPath) {
			names += "${name}/"
			outputFolder = new File(outputFolder, name)
			assert outputFolder.directory : ("Output folder $names " + (outputFolder.exists() ?
					'not a folder.' : "missing. Parent folder contents: ${outputFolder.parentFile.list()}"))
		}
		for(String fileName: shouldExist) {
			assert new File(outputFolder, "${fileName}.html").file :
					"Expected output file '$fileName' does not exist. Folder contents: ${outputFolder.listFiles()}"
		}
		for(String fileName: shouldNotExist) {
			assert !new File(outputFolder, "${fileName}.html").exists()
		}
	}
	
	private String generateBuildscript(
			String sourceFolder = DEFAULT_MARKDOWN_FOLDER,
			String destFolder = DEFAULT_HTML_FOLDER,
			String taskName = DEFAULT_TASK_NAME) {
		File buildFile = projectFolder.newFile('build.gradle')
		buildFile.text =
"""
plugins {
	id 'org.uulib.grmd.markdown-base'
}

task(${taskName}, type: MarkdownCompile) {
	srcDir '${sourceFolder}'
	destinationDir = file('${destFolder}')
}
"""
	}
			
	private BuildResult runBuild(List<String> tasks = [DEFAULT_TASK_NAME]) {
		getGradleRunner(projectFolder.root, *tasks).build()
	}

}
