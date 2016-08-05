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
package org.uulib.gradle.markdown.pegdown;

import java.lang.reflect.Method
import org.codehaus.groovy.control.CompilationFailedException
import org.uulib.gradle.markdown.pegdown.PegDownConfig;

import spock.lang.*

/**
 * A basic set of reflection tests to check that the {@code HasPegDownOptions} AST transformation has added methods to
 * the {@linkplain PegDownConfig} interface.
 */
class PegDownConfigSpec extends Specification {
	
	GroovyClassLoader groovyClassLoader = new GroovyClassLoader()
	
	@Unroll
	def "#methodName method exists"(String methodName) {
		expect:
		methodExists(methodName)
		
		where:
		methodName << ['isSmartypants', 'setAbbreviations', 'isFencedCodeBlocks', 'getOptions', 'setOptions']
	}
	
	@Unroll
	def "#methodName method does not exist"(String methodName) {
		expect:
		!methodExists(methodName)
		
		where:
		methodName << ['getNone', 'setNone']
	}
	
	private boolean methodExists(String methodName) {
		for(Method m: PegDownConfig.class.methods) {
			if(m.name==methodName) {
				return true
			}
		}
		return false
	}

}
