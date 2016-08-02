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

public abstract class IncrementalSourceTask extends DefaultTask {

	@Delegate(excludeTypes=[FileTree, Named, PatternFilterable], interfaces=false)
	@InputFiles
	final SourceDirectorySet sources;
	
	@Delegate
	private final PatternFilterable pf;
	
	public IncrementalSourceTask() {
		sources = getSDF().create("$name markdown source")
		pf = sources
	}
	
	@Inject
	protected SourceDirectorySetFactory getSDF() {
		throw new UnsupportedOperationException();
	}
	
	@TaskAction
	public void processChangedSources(IncrementalTaskInputs inputs) {
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
							deleteOutputs(dPath.relativize(path))
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
		
		sources.visit(getOutOfDateProcessor())
		
	}
	
	protected abstract FileVisitor getOutOfDateProcessor()
	protected abstract void deleteOutputs(Path input)
	protected abstract void deleteAllOutputs()

}
