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
package org.uulib.gradle.markdown.task;

import java.io.File
import java.nio.file.Path;
import java.util.Collections
import java.util.List
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.gradle.api.file.FileVisitDetails
import org.gradle.api.file.FileVisitor
import org.gradle.api.file.RelativePath
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.uulib.gradle.markdown.MarkdownProcessor
import org.uulib.gradle.markdown.Util
import org.uulib.gradle.markdown.Defaults

/**
 * Compiles markdown sources to html. The choice of the {@link MarkdownProcessor} to use is left to the
 * extending class.
 * @author Rowan Lonsdale
 */
abstract class AbstractMarkdownCompile extends IncrementalSourceTask {
	
	private static final String FILE_EXTENSION_RX = /\.[^\.\s]+$/
	
	/**
	 * The destination directory for compile HTML.
	 */
	@OutputDirectory File destinationDir
	
	/**
	 * The extension to give generated HTML files.
	 */
	@Input String htmlFileExtension = Defaults.HTML_FILE_EXTENSION
	
	protected AbstractMarkdownCompile() {
		group = Defaults.MARKDOWN_TASK_GROUP
		source.include(Util.fileExtensionsToAntPatterns(Defaults.MARKDOWN_FILE_EXTENSIONS))
	}

	@Override
	protected FileVisitor getOutOfDateProcessor() {
		return new OutOfDateProcessor()
	}

	@Override
	protected void deleteOutputs(File srcDir, Path input) {
		destinationDir.toPath().resolve(input.resolveSibling(toOutputName(input.fileName.toString()))).toFile().delete()
	}

	@Override
	protected void deleteAllOutputs() {
		getProject().delete(destinationDir.listFiles())
	}
	
	private String toOutputName(String input) {
		return input.replaceFirst(FILE_EXTENSION_RX, ".$htmlFileExtension")
	}

	/**
	 * @return The {@link MarkdownProcessor} to use to compile markdown to HTML.
	 */
	protected abstract MarkdownProcessor getMarkdownProcessor()
	
	private class OutOfDateProcessor implements FileVisitor {
		
		private final MarkdownProcessor markdownProcessor = AbstractMarkdownCompile.this.markdownProcessor

		@Override
		public void visitDir(FileVisitDetails details) {
			details.relativePath.getFile(destinationDir).mkdirs()
		}

		@Override
		public void visitFile(FileVisitDetails details) {
			RelativePath rp = details.relativePath

			File outputFile = rp.replaceLastName(toOutputName(rp.lastName))
					.getFile(destinationDir)

			outputFile.withWriter { Writer html ->
				details.file.withReader { Reader markdown ->
					markdownProcessor.markdownToHTML(markdown, html)
				}
			}

		}
		
	}

}
