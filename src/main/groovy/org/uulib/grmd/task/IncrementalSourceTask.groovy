package org.uulib.grmd.task;

import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set

import javax.inject.Inject;

import org.gradle.api.Action;
import org.gradle.api.DefaultTask
import org.gradle.api.Named;
import org.gradle.api.file.FileTree;
import org.gradle.api.file.FileTreeElement;
import org.gradle.api.file.FileVisitor;
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.file.SourceDirectorySetFactory;
import org.gradle.api.specs.Spec
import org.gradle.api.tasks.InputFiles;
import org.gradle.api.tasks.OutputDirectories;
import org.gradle.api.tasks.TaskAction;
import org.gradle.api.tasks.incremental.IncrementalTaskInputs;
import org.gradle.api.tasks.incremental.InputFileDetails;
import org.gradle.api.tasks.util.PatternFilterable;
import org.gradle.api.tasks.util.PatternSet;
import org.gradle.language.base.sources.BaseLanguageSourceSet;

/**
 * A task whose inputs are taken from source directories, and where each output file generally depend on a single
 * input file.
 * <p/>
 * This task's {@linkplain #getActions() actions} are defined in this class, and extending classes provide a function
 * to process each source file via the {@link #getOutOfDateProcessor()} method.
 * @author Rowan Lonsdale
 */
public abstract class IncrementalSourceTask extends DefaultTask {

	/**
	 * The {@linkplain SourceDirectorySet} that this task compiles.
	 */
	@Delegate(excludeTypes=[FileTree, Named, PatternFilterable], interfaces=false)
	@InputFiles
	final SourceDirectorySet sources;
	
	@Delegate
	private final PatternFilterable pf;
	
	/**
	 * Constructor for IncrementalSourceTask.
	 * @param sourceTypeName An name for the type of source that this task compiles.
	 */
	protected IncrementalSourceTask(String sourceTypeName) {
		sources = getSDF().create("$name ${sourceTypeName} source")
		pf = sources
	}
	
	/**
	 * Used to create the task's source container during construction.
	 */
	@Inject
	protected SourceDirectorySetFactory getSDF() {
		throw new UnsupportedOperationException();
	}
	
	@TaskAction
	void processChangedSources(IncrementalTaskInputs inputs) {
		List<Boolean> incremental = [inputs.isIncremental()]
		Set<File> outOfDate = new HashSet<>()
		
		if(incremental[0]) {
			inputs.outOfDate { InputFileDetails details ->
				outOfDate.add(details.file);
			}

			inputs.removed { InputFileDetails details ->
				if(incremental[0]) {
					Path path = details.getFile().toPath()
					for(File dir: sources.srcDirs) {
						Path dPath = dir.toPath()
						if(path.startsWith(dPath)) {
							deleteOutputs(dir, dPath.relativize(path))
							return
						}
					}
					incremental[0] = false;
				}
			}
		}
		
		FileTree sources = this.sources
		
		if(!incremental[0]) {
			deleteAllOutputs()
		} else {
			
			sources = sources.matching(new PatternSet().include{ FileTreeElement fte ->
				return outOfDate.contains(fte.getFile())
			});
			
		}
		
		sources.visit(outOfDateProcessor)
		
	}
	
	/**
	 * Produces a {@linkplain FileVisitor} that will visit each of the out-of-date source files and directories in this
	 * task's sources. This method will only be called once per execution of the task.
	 * @return The FileVisitor that compiles out of date sources into new outputs.
	 */
	protected abstract FileVisitor getOutOfDateProcessor()
	
	/**
	 * Deletes the output files corresponding to the specified source file, which was removed since the last run.
	 * @param srcDir The root source directory that contained the source file.
	 * @param input The {@linkplain Path} of the deleted source file relative to the root source directory.
	 */
	protected abstract void deleteOutputs(File srcDir, Path input)
	
	/**
	 * Deletes all output files.
	 */
	protected abstract void deleteAllOutputs()

}
