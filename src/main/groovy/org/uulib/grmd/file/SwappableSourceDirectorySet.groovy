package org.uulib.grmd.file

import org.gradle.api.Named;
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.file.SourceDirectorySetFactory
import org.uulib.grmd.Renamable;

/**
 * A {@linkplain SourceDirectorySet} whose contents can be replaced by that of another SourceDirectorySet,
 * and that can be renamed.
 * @author Rowan Lonsdale
 */
public final class SwappableSourceDirectorySet implements Renamable {
	
	String name
	
	@Delegate(excludeTypes=[Named])
	private final SourceDirectorySet delegate
	
	/**
	 * Constructor for SwappableSourceDirectorySet.
	 * @param factory A {@linkplain SourceDirectorySetFactory} with which to instantiate this object's delegate
	 *                container.
	 * @param initialName The initial name for this instance.
	 */
	public SwappableSourceDirectorySet(SourceDirectorySetFactory factory, String initialName) {
		Objects.requireNonNull(factory)
		Objects.requireNonNull(initialName)
		this.delegate = factory.create('delegate')
		this.name = initialName
	}
	
	/**
	 * Sets the contents of this SwappableSourceDirectorySet to that of the given {@linkplain SourceDirectorySet}.
	 * @param replacement The new contents to use.
	 * @param rename {@code true} iff the receiver should also take the name of the given replacement
	 *               (default: {@code false}).
	 */
	public void setSource(SourceDirectorySet replacement, boolean rename = false) {
		Objects.requireNonNull(replacement)
		this.delegate.srcDirs = replacement.srcDirs
		if(rename) {
			name  = replacement.name
		}
	}

}
