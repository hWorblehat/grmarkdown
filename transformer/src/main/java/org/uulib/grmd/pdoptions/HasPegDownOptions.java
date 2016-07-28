package org.uulib.grmd.pdoptions;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.codehaus.groovy.transform.GroovyASTTransformationClass;

/**
 * AST transformation to add pegdown extension getters and setters.
 * <p/>
 * This annotation can be applied to a <em>groovy</em> class or interface, which will cause it to have boolean getter
 * and setter methods for each of the options flags defined in the {@linkplain org.pegdown.Extensions} interface.
 * <p/>
 * If a class is being annotated, then it must also define a non-final integer field annotated with
 * {@linkplain PegDownOptions @PegDownOptions}. This field will be manipulated and inspected by the generated methods
 * using standard bitwise operators.
 * 
 * @see Transformer
 * 
 * @author Rowan Lonsdale
 */
@Retention(SOURCE)
@Target(TYPE)
@GroovyASTTransformationClass(classes=Transformer.class)
public @interface HasPegDownOptions {

}
