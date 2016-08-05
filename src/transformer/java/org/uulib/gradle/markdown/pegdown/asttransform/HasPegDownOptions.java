/*
 * Copyright (c) 2016 Rowan Lonsdale.
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.uulib.gradle.markdown.pegdown.asttransform;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

import java.lang.annotation.Documented;
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
@Documented
@Retention(SOURCE)
@Target(TYPE)
@GroovyASTTransformationClass(classes=Transformer.class)
public @interface HasPegDownOptions {

}
