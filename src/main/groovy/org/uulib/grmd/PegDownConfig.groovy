package org.uulib.grmd

import org.pegdown.PegDownProcessor
import org.uulib.grmd.pdoptions.HasPegDownOptions

import groovy.transform.ASTTest;

/**
 * Configuration options for a {@linkplain PegDownProcessor}.
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
