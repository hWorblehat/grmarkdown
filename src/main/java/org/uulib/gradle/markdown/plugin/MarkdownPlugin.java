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

import java.io.File;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.PluginManager;
import org.gradle.language.base.plugins.LifecycleBasePlugin;
import org.gradle.model.ModelMap;
import org.gradle.model.Mutate;
import org.gradle.model.Path;
import org.gradle.model.RuleSource;
import org.uulib.gradle.markdown.task.MarkdownCompile;

/**
 * Convention plugin for compiling markdown in a "traditional" Gradle project configuration:
 * <ul>
 *   <li> Applies the {@link MarkdownBasePlugin} and {@link LifecycleBasePlugin} plugins.
 *   <li> Creates a new {@link MarkdownCompile} task that compiles markdown
 *         under {@link #DEFAULT_SRC_DIR} into {@link #DEFAULT_DESTINATION_DIR} in the project build directory.
 *   <li> Sets this task as a dependency of the {@link LifecycleBasePlugin#ASSEMBLE_TASK_NAME assemble task}.
 * </ul>
 * @author Rowan Lonsdale
 */
public class MarkdownPlugin implements Plugin<Project> {
	
	/**
	 * The name of the {@link MarkdownCompile} task this plugin creates.
	 */
	public static final String MARKDOWN_TASK_NAME = "compileMarkdown";
	public static final String DEFAULT_SRC_NAME = "docs";
	
	/**
	 * The default source directory where the {@link MarkdownCompile} task looks for markdown files.
	 */
	public static final String DEFAULT_SRC_DIR = "src/docs/markdown";
	
	/**
	 * The default output directory for the {@link MarkdownCompile} task that this plugin creates.
	 */
	public static final String DEFAULT_DESTINATION_DIR = "docs/html";

	@Override
	public void apply(Project project) {
		PluginManager pluginManager = project.getPluginManager();
		pluginManager.apply(MarkdownBasePlugin.class);
		pluginManager.apply(LifecycleBasePlugin.class);
	}
	
	static class Rules extends RuleSource {
		
		@Mutate
		void addMarkdownCompileTask(ModelMap<Task> tasks, @Path("buildDir") final File buildDir) {
			tasks.create(MARKDOWN_TASK_NAME, MarkdownCompile.class, new Action<MarkdownCompile>() {
				@Override public void execute(MarkdownCompile task){
					task.srcDir(DEFAULT_SRC_DIR);
					task.setDestinationDir(new File(buildDir, DEFAULT_DESTINATION_DIR));
					task.setSourceName(DEFAULT_SRC_NAME);
				}
			});
			
			tasks.named(LifecycleBasePlugin.ASSEMBLE_TASK_NAME, new Action<Task>() {
				@Override public void execute(Task assemble) {
					assemble.dependsOn(MARKDOWN_TASK_NAME);
				}
			});
		}
		
	}

}
