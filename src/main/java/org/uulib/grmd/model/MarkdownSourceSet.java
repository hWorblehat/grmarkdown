package org.uulib.grmd.model;

import org.gradle.language.base.LanguageSourceSet;
import org.gradle.model.Managed;
import org.gradle.model.Unmanaged;
import org.uulib.grmd.pegdown.PegDownConfig;

/**
 * A {@linkplain LanguageSourceSet} for markdown sources.
 * @author Rowan Lonsdale
 */
@Managed
public interface MarkdownSourceSet extends LanguageSourceSet {
	
	@Unmanaged
	PegDownConfig getProcessorConfig();
	
	void setProcessorConfig(PegDownConfig config);
	
}
