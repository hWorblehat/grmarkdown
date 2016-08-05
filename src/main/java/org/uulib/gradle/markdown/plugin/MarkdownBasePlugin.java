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
package org.uulib.gradle.markdown.plugin;

import java.util.List;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.ExtensionAware;
import org.gradle.api.plugins.ExtraPropertiesExtension;
import org.gradle.model.Finalize;
import org.gradle.model.ModelMap;
import org.gradle.model.Mutate;
import org.gradle.model.RuleSource;
import org.uulib.gradle.markdown.Defaults;
import org.uulib.gradle.markdown.task.AbstractMarkdownCompile;
import org.uulib.gradle.markdown.task.MarkdownCompile;

import com.google.common.collect.ImmutableList;

/**
 * Base Gradle plugin for markdown:
 * <ul>
 *   <li/> Adds the {@linkplain MarkdownCompile} and {@linkplain AbstractMarkdownCompile} tasks class as extra
 *         properties to the project, so that new tasks of these types can be defined in the build script.
 *   <li/> Applies a rule that sets a default description for all tasks of type AbstractMarkdownCompile.
 * </ul>
 * 
 * @author Rowan Lonsdale
 */
public class MarkdownBasePlugin implements Plugin<Project>, Defaults {
	
	private static final ImmutableList<Class<? extends Task>> TASK_CLASSES =
			ImmutableList.<Class<? extends Task>>of(MarkdownCompile.class, AbstractMarkdownCompile.class);

	@Override
	public void apply(Project project) {
		setClassesAsExtraProperties(project, TASK_CLASSES);
	}
	
	static void setClassesAsExtraProperties(ExtensionAware owner, List<? extends Class<?>> classes) {
		ExtraPropertiesExtension ext = owner.getExtensions().getExtraProperties();
		for(Class<?> clazz: classes) {
			ext.set(clazz.getSimpleName(), clazz);
		}
	}
	
	static class Rules extends RuleSource {
		
		@Mutate
		void applyTaskRules(ModelMap<Task> tasks) {
			tasks.withType(AbstractMarkdownCompile.class, MarkdownTaskRules.class);
		}
		
		static class MarkdownTaskRules extends RuleSource {
			
			@Finalize
			void setTaskDescription(AbstractMarkdownCompile task) {
				if(task.getDescription()==null || task.getDescription().isEmpty()) {
					StringBuilder description = new StringBuilder("Compiles ");
					String sourceName = task.getSourceName();
					if(sourceName!=null && !sourceName.isEmpty()) {
						description.append("the ").append(sourceName).append(' ');
					}
					description.append("markdown source.");
					task.setDescription(description.toString());
				}
			}
			
		}
		
	}

}
