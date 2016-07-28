package org.uulib.grmd.pdoptions;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Informs the {@linkplain Transformer} that the annotated field should be used to store pegdown extension option flags.
 * The annotated field must be non-final and an integer.
 * @see HasPegDownOptions
 * @author Rowan Lonsdale
 */
@Retention(SOURCE)
@Target(FIELD)
public @interface PegDownOptions {

}
