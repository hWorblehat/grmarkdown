package org.uulib.grmd.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.uulib.grmd.task.MarkdownCompile;

public class BasePlugin implements Plugin<Project> {
	
	private static final Class<? extends Task> TASK_CLASS = MarkdownCompile.class;

	@Override
	public void apply(Project project) {
		project.getExtensions().getExtraProperties().set(TASK_CLASS.getSimpleName(), TASK_CLASS);
	}

}
