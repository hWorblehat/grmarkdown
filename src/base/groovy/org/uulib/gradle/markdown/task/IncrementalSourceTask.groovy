package org.uulib.gradle.markdown.task;

import java.nio.file.Path

import javax.inject.Inject

import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.file.DirectoryTree
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileTreeElement
import org.gradle.api.file.FileVisitor
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.file.SourceDirectorySetFactory
import org.gradle.api.specs.Spec
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectories
import org.gradle.api.tasks.TaskAction
import org.gradle.api.tasks.incremental.IncrementalTaskInputs
import org.gradle.api.tasks.incremental.InputFileDetails
import org.gradle.api.tasks.util.PatternFilterable
import org.gradle.api.tasks.util.PatternSet
import org.gradle.language.base.sources.BaseLanguageSourceSet

/**
 * A task whose inputs are taken from source directories, and where each output file generally depend on a single
 * input file.
 * <p/>
 * This task's {@linkplain #getActions() actions} are defined in this class, and extending classes provide a function
 * to process each source file via the {@link #getOutOfDateProcessor()} method.
 * @author Rowan Lonsdale
 */
public abstract class IncrementalSourceTask extends DefaultTask {

	private final SourceDirectorySet source
	
	@Delegate
	private final PatternFilterable pf
	
	String sourceName
	
	/**
	 * Constructor for IncrementalSourceTask.
	 * @param sourceTypeName An name for the type of source that this task compiles.
	 */
	protected IncrementalSourceTask() {
		source = getSDF().create("${name} sources")
		pf = source.filter
	}
	
	/**
	 * Used to create the task's source container during construction.
	 */
	@Inject
	protected SourceDirectorySetFactory getSDF() {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * The {@linkplain SourceDirectorySet} that this task compiles.
	 */
	@InputFiles
	FileTree getSource() {
		return source
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
					for(File dir: source.srcDirs) {
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
		
		FileTree sources = this.source
		
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
	 * @return The source directories whose contents will be compiled by this task.
	 */
	Set<File> getSrcDirs() {
		source.srcDirs
	}
	
	/**
	 * Sets the source directories whose contents this task will compile.
	 * @param srcPaths The source directories. These may either be a {@linkplain SourceDirectorySet}, or are else
	 *                 evaluated as per {@link Project#file(Object)}.
	 */
	void setSrcDirs(Iterable<?> srcPaths) {
		source.srcDirs = srcPaths
	}
	
	/**
	 * @return The source directory trees that this task will compile, or an empty set when this task has no sources.
	 */
	Set<DirectoryTree> getSrcDirTrees() {
		source.srcDirTrees
	}
	
	/**
	 * Adds the given source to be compiled by this task.
	 * @param source The additional source to be compiled by this task.
	 */
	void source(SourceDirectorySet source) {
		this.source.source(source)
	}
	
	/**
	 * Adds the given source directory to be compiled by this task.
	 * @param srcPath The additional source directory, evaluated as per {@link Project#file(Object)}.
	 */
	void srcDir(Object srcPath) {
		source.srcDir(srcPath)
	}
	
	/**
	 * Adds the given source directories to be compiled by this task.
	 * @param srcPath The additional source directories, evaluated as per {@link Project#file(Object)}.
	 */
	void srcDirs(Object... srcPaths) {
		source.srcDirs(srcPaths)
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
