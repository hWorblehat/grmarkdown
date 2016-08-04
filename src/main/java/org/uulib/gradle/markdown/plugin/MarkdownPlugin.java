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
 *   <li/> Applies the {@linkplain MarkdownBasePlugin} and {@linkplain LifecycleBasePlugin} plugins.
 *   <li/> Creates a new {@linkplain MarkdownCompile} task named {@value #MARKDOWN_TASK_NAME} that compiles markdown
 *         under {@value #DEFAULT_SRC_DIR} into {@value #DEFAULT_DESTINATION_DIR} in the project build directory.
 *   <li/> Sets {@value #MARKDOWN_TASK_NAME} as a dependency of the
 *         {@linkplain LifecycleBasePlugin#ASSEMBLE_TASK_NAME assemble task}.
 * </ul>
 * @author Rowan Lonsdale
 */
public class MarkdownPlugin implements Plugin<Project> {
	
	public static final String MARKDOWN_TASK_NAME = "compileMarkdown";
	public static final String DEFAULT_SRC_NAME = "docs";
	public static final String DEFAULT_SRC_DIR = "src/docs/markdown";
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
