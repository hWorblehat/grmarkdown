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
