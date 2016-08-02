package org.uulib.grmd;

import org.gradle.api.Named;

/**
 * Represents an object with a mutable name.
 * @author Rowan Lonsdale
 */
public interface Renamable extends Named {
	
	/**
	 * Sets this objet's name.
	 * @param name The new name to use.
	 */
	public void setName(String name);

}
