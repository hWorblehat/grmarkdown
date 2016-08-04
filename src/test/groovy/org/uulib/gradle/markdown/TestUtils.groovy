package org.uulib.gradle.markdown;

import org.gradle.testkit.runner.GradleRunner;

class TestUtils {
	
	private TestUtils(){}
	
	static final String sampleMarkdownText =
"""
# Test Document
This is a test document to check the markdown compiler is working:

 * An HTML file with the same name should be created
 * That HTML file should contain HTML compiled by [Pegdown](http://pegdown.org)
"""

	static final String pluginIdBase
	
	static {
		Properties props = new Properties()
		new File('gradle.properties').withReader {
			props.load(it)
		}
		pluginIdBase = props.getProperty('group')
	}
	
	static GradleRunner getGradleRunner(File projectDir, String... tasks) {
		GradleRunner.create()
				.withPluginClasspath()
				.withDebug(true)
				.withProjectDir(projectDir)
				.withArguments('--stacktrace', *tasks)
	}

}
