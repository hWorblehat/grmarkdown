package org.uulib.grmd.task;

import java.io.File
import java.nio.file.Path;
import java.util.Collections
import java.util.List
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.gradle.api.file.FileVisitDetails
import org.gradle.api.file.FileVisitor
import org.gradle.api.file.RelativePath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.uulib.grmd.MarkdownProcessor

abstract class AbstractMarkdownCompile extends IncrementalSourceTask {
	
	private static final String FILE_EXTENSION_RX = /\.[^\.\s]+$/
	
	@OutputDirectory File destinationDir
	@Input String htmlFileExtension = 'html'
	
	protected AbstractMarkdownCompile() {
		include("**/*.md", "**/*.markdown")
	}

	@Override
	protected FileVisitor getOutOfDateProcessor() {
		return new OutOfDateProcessor()
	}

	@Override
	protected void deleteOutputs(Path input) {
		destinationDir.toPath().resolve(input.resolveSibling(toOutputName(input.fileName.toString()))).toFile().delete()
	}

	@Override
	protected void deleteAllOutputs() {
		getProject().delete(destinationDir.listFiles())
	}
	
	private String toOutputName(String input) {
		return input.replaceFirst(FILE_EXTENSION_RX, ".$htmlFileExtension")
	}

	protected abstract MarkdownProcessor getMarkdownProcessor()
	
	private class OutOfDateProcessor implements FileVisitor {
		
		private final MarkdownProcessor markdownProcessor = AbstractMarkdownCompile.this.markdownProcessor

		@Override
		public void visitDir(FileVisitDetails details) {
			details.relativePath.getFile(destinationDir).mkdirs()
		}

		@Override
		public void visitFile(FileVisitDetails details) {
			RelativePath rp = details.relativePath

			File outputFile = rp.replaceLastName(toOutputName(rp.lastName))
					.getFile(destinationDir)

			outputFile.withWriter { Writer html ->
				details.file.withReader { Reader markdown ->
					markdownProcessor.markdownToHTML(markdown, html)
				}
			}

		}
		
	}

}
