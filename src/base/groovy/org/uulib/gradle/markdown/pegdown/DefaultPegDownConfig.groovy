package org.uulib.gradle.markdown.pegdown

import org.uulib.gradle.markdown.pegdown.PegDownConfig;
import org.uulib.gradle.markdown.pegdown.asttransform.HasPegDownOptions
import org.uulib.gradle.markdown.pegdown.asttransform.PegDownOptions;;

/**
 * Default implementation of {@linkplain PegDownConfig}.
 * @author Rowan Lonsdale
 */
@HasPegDownOptions
class DefaultPegDownConfig implements PegDownConfig {
	
	@PegDownOptions
	int options

}
