package org.uulib.gradle.markdown.plugin;

import java.io.File;

import org.gradle.api.Action;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.language.base.plugins.ComponentModelBasePlugin;
import org.gradle.model.Defaults;
import org.gradle.model.Each;
import org.gradle.model.ModelMap;
import org.gradle.model.Path;
import org.gradle.model.RuleInput;
import org.gradle.model.RuleSource;
import org.gradle.model.RuleTarget;
import org.gradle.model.Rules;
import org.gradle.platform.base.BinaryTasks;
import org.gradle.platform.base.ComponentBinaries;
import org.gradle.platform.base.ComponentSpec;
import org.gradle.platform.base.ComponentType;
import org.gradle.platform.base.TypeBuilder;
import org.gradle.platform.base.VariantComponentSpec;
import org.uulib.gradle.markdown.Util;
import org.uulib.gradle.markdown.model.DocumentationSpec;
import org.uulib.gradle.markdown.model.HtmlBinary;
import org.uulib.gradle.markdown.model.MarkdownSourceSet;
import org.uulib.gradle.markdown.pegdown.DefaultPegDownConfig;
import org.uulib.gradle.markdown.task.MarkdownCompile;

import com.google.common.collect.ImmutableList;

public class MarkdownModelPlugin implements Plugin<Project>, org.uulib.gradle.markdown.Defaults {
	
	private static final ImmutableList<Class<? extends ComponentSpec>> MODEL_TYPES =
			ImmutableList.of(DocumentationSpec.class, HtmlBinary.class, MarkdownSourceSet.class);

	@Override
	public void apply(Project project) {
		project.getPluginManager().apply(MarkdownBasePlugin.class);
		project.getPluginManager().apply(ComponentModelBasePlugin.class);
		
		MarkdownBasePlugin.setClassesAsExtraProperties(project, MODEL_TYPES);
	}
	
	static class ProjectRules extends RuleSource {
		
		@ComponentType
		void registerDocumentationComponent(TypeBuilder<DocumentationSpec> builder) {}
		
		@ComponentType
		void registerHtmlBinary(TypeBuilder<HtmlBinary> builder) {}
		
		@ComponentType
		void registerMarkdownSource(TypeBuilder<MarkdownSourceSet> builder) {}
		
		@Defaults
		void createPegDownConfig(@Each MarkdownSourceSet sourceSet) {
			sourceSet.setProcessorConfig(new DefaultPegDownConfig());
		}
		
		@Defaults
		void addMarkdownSourceToDocComponents(@Each DocumentationSpec documentationSpec) {
			documentationSpec.getSources().create("markdown", MarkdownSourceSet.class);
		}
		
		@Rules
		void setHtmlOutputDir(HtmlBinaryOutputDirRules rules,
				@Each VariantComponentSpec component, @Path("buildDir") File buildDir) {
			rules.setBinaries(component.getBinaries().withType(HtmlBinary.class));
			rules.setComponentBuildDir(new File(buildDir, component.getName()));
		}
		
		static abstract class HtmlBinaryOutputDirRules extends RuleSource {
			
			@RuleTarget
			abstract ModelMap<HtmlBinary> getBinaries();
			abstract void setBinaries(ModelMap<HtmlBinary> binaries);
			
			@RuleInput
			abstract File getComponentBuildDir();
			abstract void setComponentBuildDir(File buildDir);
			
			@Defaults
			void setOutputDir(@Each HtmlBinary binary) {
				binary.setOutputDir(new File(getComponentBuildDir(), binary.getName()));
			}
			
		}
		
		@ComponentBinaries
		void generateHtmlBinaries(ModelMap<HtmlBinary> binaries, DocumentationSpec doumentationSpec) {
			binaries.create("htmlDocument");
		}
		
		@Defaults
		void setHtmlFileExtension(@Each HtmlBinary binary) {
			binary.setHtmlFileExtension(HTML_FILE_EXTENSION);
		}
		
		@Defaults
		void setMarkdownSourceIncludes(@Each MarkdownSourceSet markdown) {
			markdown.getSource().include(Util.fileExtensionsToAntPatterns(MARKDOWN_FILE_EXTENSIONS));
		}
		
		@BinaryTasks
		void generateCompileMarkdownTasks(final ModelMap<Task> tasks, final HtmlBinary html) {
			for(final MarkdownSourceSet markdown: html.getInputs().withType(MarkdownSourceSet.class)){
				String taskName = html.getTasks().taskName("compile", markdown.getName());
				tasks.create(taskName, MarkdownCompile.class, new Action<MarkdownCompile>() {
					@Override public void execute(MarkdownCompile task) {
						task.source(markdown.getSource());
						task.setDestinationDir(html.getOutputDir());
						task.setHtmlFileExtension(html.getHtmlFileExtension());
						task.setOptions(markdown.getProcessorConfig().getOptions());
					}
				});
			}
		}
		
	}

}
