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
