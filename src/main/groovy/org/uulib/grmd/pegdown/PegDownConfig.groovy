package org.uulib.grmd.pegdown

import org.pegdown.PegDownProcessor
import org.uulib.grmd.pegdown.asttransform.HasPegDownOptions

/**
 * Configuration options for a {@linkplain PegDownProcessor}.
 * <p/>
 * Note that the presence of the {@link HasPegDownOptions @HasPegDownOptions} annotation means that this interface also
 * defines an {@code is&lt;Extension>()} and {@code set&ltExtension>(boolean)} method for each of the extensions
 * defined in {@linkplain org.pegdown.Extensions}.
 * 
 * @author Rowan Lonsdale
 */
@HasPegDownOptions
interface PegDownConfig {
	
	/**
	 * @return The options flags to pass to a {@linkplain PegDownProcessor}.
	 */
	int getOptions()
	
	/**
	 * Sets the options flags to pass to the {@linkplain PegDownProcessor}.
	 * @param options The options to set.
	 */
	void setOptions(int options)

}
