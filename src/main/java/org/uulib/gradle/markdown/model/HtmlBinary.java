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
package org.uulib.gradle.markdown.model;

import java.io.File;

import org.gradle.model.Managed;
import org.gradle.platform.base.BinarySpec;

/**
 * A generated directory structure of HTML files.
 * @author Rowan Lonsdale
 */
@Managed
public interface HtmlBinary extends BinarySpec {
	
	/**
	 * @return The directory into which this binary will be generated.
	 */
	File getOutputDir();
	
	/**
	 * Sets the directory into which this binary will be generated.
	 * @param outputDir The output directory to use.
	 */
	void setOutputDir(File outputDir);
	
	/**
	 * @return The file extension to use for generated HTML files.
	 */
	String getHtmlFileExtension();
	
	/**
	 * Sets the file extension to use for generated HTML files.
	 * @param extension The file extension to use.
	 */
	void setHtmlFileExtension(String extension);

}
