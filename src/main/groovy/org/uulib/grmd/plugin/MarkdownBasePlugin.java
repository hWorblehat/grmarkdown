package org.uulib.grmd.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.model.Finalize;
import org.gradle.model.ModelMap;
import org.gradle.model.Mutate;
import org.gradle.model.RuleSource;
import org.uulib.grmd.task.AbstractMarkdownCompile;
import org.uulib.grmd.task.MarkdownCompile;

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
public class MarkdownBasePlugin implements Plugin<Project> {
	
	public static final String MARKDOWN_TASK_GROUP = "Documentation";
	
	private static final ImmutableList<Class<? extends Task>> TASK_CLASSES =
			ImmutableList.<Class<? extends Task>>of(MarkdownCompile.class, AbstractMarkdownCompile.class);

	@Override
	public void apply(Project project) {
		for(Class<? extends Task> taskClass: TASK_CLASSES) {
			project.getExtensions().getExtraProperties().set(taskClass.getSimpleName(), taskClass);
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
					String sourceName = task.getSource().getName();
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
