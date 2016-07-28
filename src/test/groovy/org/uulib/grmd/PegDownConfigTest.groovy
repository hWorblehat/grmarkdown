package org.uulib.grmd;

import java.lang.reflect.Method
import org.codehaus.groovy.control.CompilationFailedException;

import spock.lang.*

/**
 * A basic set of reflection tests to check that the {@code HasPegDownOptions} AST transformation has added methods to
 * the {@linkplain PegDownConfig} interface.
 */
class PegDownConfigTest extends Specification {
	
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
