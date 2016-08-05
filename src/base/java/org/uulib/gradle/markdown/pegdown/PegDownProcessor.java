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

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.uulib.gradle.markdown.MarkdownProcessor;

/**
 * A {@link MarkdownProcessor} that uses <a href="http://pegdown.org">Pegdown</a> to compile markdown.
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
