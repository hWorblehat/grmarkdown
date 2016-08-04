package org.uulib.gradle.markdown

final class Util {
	
	private Util() {}
	
	static String fileExtensionToAntPattern(String extension) {
		"**/*.${extension}"
	}
	
	static List<String> fileExtensionsToAntPatterns(Collection<String> extensions) {
		extensions.collect { fileExtensionToAntPattern(it) }
	}

}
