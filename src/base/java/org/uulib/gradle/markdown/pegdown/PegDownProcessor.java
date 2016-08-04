package org.uulib.gradle.markdown.pegdown;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.uulib.gradle.markdown.MarkdownProcessor;

/**
 * A {@linkplain MarkdownProcessor} that uses <a href="http://pegdown.org">Pegdown</a> to compile markdown.
 * 
 * @author Rowan Lonsdale
 */
public class PegDownProcessor implements MarkdownProcessor {
	
	private static final Object PEGDOWN_LOCK = new Object();
	
	private final org.pegdown.PegDownProcessor processor;
	
	public PegDownProcessor(MinimalPegDownConfig config) {
		processor = new org.pegdown.PegDownProcessor(config.getOptions());
	}

	@Override
	public void markdownToHTML(Reader markdown, Writer html) throws IOException {
		StringWriter sw = new StringWriter();
		IOUtils.copy(markdown, sw);
		String htmlString = markdownToHTML(sw.toString());
		IOUtils.copy(new StringReader(htmlString), html);
	}
	
	/**
	 * Compiles the given markown to HTML.
	 * @param markdown The markdown to compile.
	 * @return The resulting HTML.
	 */
	public String markdownToHTML(String markdown) {
		synchronized (PEGDOWN_LOCK) {
			return processor.markdownToHtml(markdown);
		}
	}

}
