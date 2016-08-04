package org.uulib.gradle.markdown;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Converts markdown (of some form) to HTML.
 * @author Rowan Lonsdale
 */
public interface MarkdownProcessor {
	
	/**
	 * Compiles the given markdown to HTML.
	 * @param markdown The source of markdown text to compile.
	 * @param html The sink to write HTML to.
	 * @throws IOException if an I/O error occurs.
	 */
	void markdownToHTML(Reader markdown, Writer html) throws IOException;

}
