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

import org.gradle.model.Managed;
import org.gradle.platform.base.ComponentSpec;
import org.gradle.platform.base.GeneralComponentSpec;

/**
 * A {@link ComponentSpec} for a documentation component.
 * @author Rowan Lonsdale
 */
@Managed
public interface DocumentationSpec extends GeneralComponentSpec {}
