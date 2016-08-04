package org.uulib.gradle.markdown.pegdown;

/**
 * Configuration options for a {@linkplain PegDownProcessor}.
 * 
 * @author Rowan Lonsdale
 */
public interface MinimalPegDownConfig {
	
	/**
	 * @return The options flags to pass to a {@linkplain PegDownProcessor}.
	 */
	int getOptions();
	
	/**
	 * Sets the options flags to pass to the {@linkplain PegDownProcessor}.
	 * @param options The options to set.
	 */
	void setOptions(int options);

}
