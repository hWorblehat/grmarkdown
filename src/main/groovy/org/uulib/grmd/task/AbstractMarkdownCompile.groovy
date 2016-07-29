package org.uulib.grmd.task

import java.io.File

import org.apache.commons.io.output.CountingOutputStream;
import org.gradle.api.file.FileVisitDetails
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import org.gradle.api.tasks.incremental.InputFileDetails

import org.uulib.grmd.MarkdownProcessor

abstract class AbstractMarkdownCompile extends SourceTask {
	
	@OutputDirectory File destinationDir
	
	AbstractMarkdownCompile() {
		include "**/*.md", "**/*.markdown"
	}
	
	@TaskAction
	void compile(IncrementalTaskInputs inputs) {
		if(!inputs.incremental) {
			project.delete(destinationDir.listFiles())
		}
		
		MarkdownProcessor processor = markdownProcessor
		IncrementalSources sources = new IncrementalSources(source, inputs)
		
		sources.outOfDate.visit { FileVisitDetails outOfDate ->
			File destinationFile = new File(destinationDir, outOfDate.path)
			if(outOfDate.directory) {
				destinationFile.mkdirs()
			} else {
				destinationFile = new File(destinationFile.parentFile, destinationFile.name.replaceFirst(/\.\w+$/, '.html'))
				destinationFile.withWriter { Writer html ->
					outOfDate.file.withReader { Reader markdown ->
						processor.markdownToHTML(markdown, html)
					}
				}
			}
		}
		
		sources.removed.visit {
			new File(destinationDir, it.path).delete()
		}
	}
	
	protected abstract MarkdownProcessor getMarkdownProcessor()

}
