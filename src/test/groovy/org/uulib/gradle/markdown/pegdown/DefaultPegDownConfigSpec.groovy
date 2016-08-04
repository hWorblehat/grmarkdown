package org.uulib.gradle.markdown.pegdown

import org.pegdown.Extensions

import spock.lang.*

@Stepwise
class DefaultPegDownConfigSpec extends Specification {
	
	@Shared PegDownConfig config = new DefaultPegDownConfig()
	
	def "Setting smartypants results in quotes and smarts being set"() {
		when:
		config.smartypants = true
		
		then:
		config.smarts
		config.quotes
		config.smartypants
		config.options == Extensions.SMARTYPANTS
	}
	
	def "Setting fencedCodeBlocks results in fencedCodeBlocks being set"(){	
		when:
		config.fencedCodeBlocks = true
		
		then:
		config.fencedCodeBlocks
		config.quotes
		config.options == (Extensions.SMARTYPANTS | Extensions.FENCED_CODE_BLOCKS)
	}
	
	def "Unsetting quotes results in quotes not being set"() {
		when:
		config.quotes = false
		
		then:
		config.smarts
		!config.quotes
		!config.abbreviations
		config.options == (Extensions.SMARTS | Extensions.FENCED_CODE_BLOCKS)
	}

}
