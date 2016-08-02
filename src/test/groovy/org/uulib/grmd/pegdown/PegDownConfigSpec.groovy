package org.uulib.grmd.pegdown;

import java.lang.reflect.Method
import org.codehaus.groovy.control.CompilationFailedException
import org.uulib.grmd.pegdown.PegDownConfig;

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
