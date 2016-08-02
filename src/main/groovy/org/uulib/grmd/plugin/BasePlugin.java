package org.uulib.grmd.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.uulib.grmd.task.MarkdownCompile;

/**
 * Base Gradle plugin for markdown. Adds the {@linkplain MarkdownCompile} task class as an extra property to the
 * project, so that new tasks of this type can be defined in the build script.
 * 
 * @author Rowan Lonsdale
 */
public class BasePlugin implements Plugin<Project> {
	
	public static final String MARKDOWN_TASK_GROUP = "Documentation";
	
	private static final Class<? extends Task> TASK_CLASS = MarkdownCompile.class;

	@Override
	public void apply(Project project) {
		project.getExtensions().getExtraProperties().set(TASK_CLASS.getSimpleName(), TASK_CLASS);
	}

}
