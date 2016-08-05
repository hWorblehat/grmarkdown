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

import org.pegdown.PegDownProcessor
import org.uulib.gradle.markdown.pegdown.asttransform.HasPegDownOptions

/**
 * Configuration options for a {@link PegDownProcessor}.
 * <p/>
 * Note that the presence of the {@link HasPegDownOptions} annotation means that this interface also
 * defines an {@code is&lt;Extension>()} and {@code set&ltExtension>(boolean)} method for each of the extensions
 * defined in {@link org.pegdown.Extensions}.
 * 
 * @author Rowan Lonsdale
 */
@HasPegDownOptions
interface PegDownConfig extends MinimalPegDownConfig {}
