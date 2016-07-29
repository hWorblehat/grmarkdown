package org.uulib.grmd.pegdown

import org.uulib.grmd.pegdown.PegDownConfig;
import org.uulib.grmd.pegdown.asttransform.HasPegDownOptions
import org.uulib.grmd.pegdown.asttransform.PegDownOptions;;

/**
 * Default implementation of {@linkplain PegDownConfig}.
 * @author Rowan Lonsdale
 */
@HasPegDownOptions
class DefaultPegDownConfig implements PegDownConfig {
	
	@PegDownOptions
	int options

}
