package org.uulib.grmd

import org.uulib.grmd.pdoptions.HasPegDownOptions
import org.uulib.grmd.pdoptions.PegDownOptions;;

/**
 * Default implementation of {@linkplain PegDownConfig}.
 * @author Rowan Lonsdale
 */
@HasPegDownOptions
class DefaultPegDownConfig implements PegDownConfig {
	
	@PegDownOptions
	int options

}
