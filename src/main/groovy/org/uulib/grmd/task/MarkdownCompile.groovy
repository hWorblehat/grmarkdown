package org.uulib.grmd.task;

import org.gradle.api.tasks.Input

import org.uulib.grmd.MarkdownProcessor
import org.uulib.grmd.pegdown.PegDownConfig
import org.uulib.grmd.pegdown.PegDownProcessor
import org.uulib.grmd.pegdown.asttransform.HasPegDownOptions
import org.uulib.grmd.pegdown.asttransform.PegDownOptions

@HasPegDownOptions
public class MarkdownCompile extends AbstractMarkdownCompile implements PegDownConfig {
	
	@PegDownOptions
	@Input int options
	
	@Override
	protected MarkdownProcessor getMarkdownProcessor() {
		return new PegDownProcessor(this)
	}

}
