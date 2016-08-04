package org.uulib.gradle.markdown.task;

import org.gradle.api.tasks.Input

import org.uulib.gradle.markdown.MarkdownProcessor
import org.uulib.gradle.markdown.pegdown.PegDownConfig
import org.uulib.gradle.markdown.pegdown.PegDownProcessor
import org.uulib.gradle.markdown.pegdown.asttransform.HasPegDownOptions
import org.uulib.gradle.markdown.pegdown.asttransform.PegDownOptions

@HasPegDownOptions
public class MarkdownCompile extends AbstractMarkdownCompile implements PegDownConfig {
	
	@PegDownOptions
	@Input int options
	
	@Override
	protected MarkdownProcessor getMarkdownProcessor() {
		return new PegDownProcessor(this)
	}

}
