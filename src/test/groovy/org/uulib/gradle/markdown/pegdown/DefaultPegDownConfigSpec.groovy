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
package org.uulib.gradle.markdown.pegdown

import org.pegdown.Extensions

import spock.lang.*

@Stepwise
class DefaultPegDownConfigSpec extends Specification {
	
	@Shared PegDownConfig config = new DefaultPegDownConfig()
	
	def "Setting smartypants results in quotes and smarts being set"() {
		when:
		config.smartypants = true
		
		then:
		config.smarts
		config.quotes
		config.smartypants
		config.options == Extensions.SMARTYPANTS
	}
	
	def "Setting fencedCodeBlocks results in fencedCodeBlocks being set"(){	
		when:
		config.fencedCodeBlocks = true
		
		then:
		config.fencedCodeBlocks
		config.quotes
		config.options == (Extensions.SMARTYPANTS | Extensions.FENCED_CODE_BLOCKS)
	}
	
	def "Unsetting quotes results in quotes not being set"() {
		when:
		config.quotes = false
		
		then:
		config.smarts
		!config.quotes
		!config.abbreviations
		config.options == (Extensions.SMARTS | Extensions.FENCED_CODE_BLOCKS)
	}

}
