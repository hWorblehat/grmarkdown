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

import com.google.common.collect.ImmutableList;

public interface Defaults {

	String MARKDOWN_TASK_GROUP = "Documentation";
	String HTML_FILE_EXTENSION = "html";
	ImmutableList<String> MARKDOWN_FILE_EXTENSIONS = ImmutableList.of("markdown", "md");

}