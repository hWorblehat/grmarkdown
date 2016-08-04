package org.uulib.gradle.markdown.pegdown

import org.pegdown.PegDownProcessor
import org.uulib.gradle.markdown.pegdown.asttransform.HasPegDownOptions

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
interface PegDownConfig extends MinimalPegDownConfig {}
