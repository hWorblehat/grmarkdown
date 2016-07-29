package org.uulib.grmd.task;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.gradle.api.Action;
import org.gradle.api.file.FileTree;
import org.gradle.api.file.FileTreeElement;
import org.gradle.api.specs.Spec;
import org.gradle.api.specs.Specs;
import org.gradle.api.tasks.incremental.IncrementalTaskInputs;
import org.gradle.api.tasks.incremental.InputFileDetails;
import org.gradle.api.tasks.util.PatternFilterable;
import org.gradle.api.tasks.util.PatternSet;

public final class IncrementalSources {
	private static final PatternFilterable ACCEPT_NONE = new PatternSet().include(Specs.<FileTreeElement>satisfyNone());
	
	private final FileTree sources;
	private final IncrementalTaskInputs inputs;
	
	private FileTree outOfDate, removed;
	
	public IncrementalSources(FileTree sources, IncrementalTaskInputs inputs) {
		this.sources = sources;
		this.inputs = inputs;
	}
	
	public FileTree getOutOfDate() {
		if(outOfDate==null) {
			if(inputs.isIncremental()) {
				InSetFilter filter = new InSetFilter();
				inputs.outOfDate(filter);
				outOfDate = filter.filter(sources);
			} else {
				outOfDate = sources;
			}
		}
		
		return outOfDate;
	}
	
	public FileTree getRemoved() {
		if(removed==null) {
			InSetFilter filter = new InSetFilter();
			if(inputs.isIncremental()) {
				inputs.removed(filter);
			}
			removed = filter.filter(sources);
		}
		
		return removed;
	}
	
	private static class InSetFilter implements Spec<FileTreeElement>, Action<InputFileDetails> {
		
		private final Set<File> included = new HashSet<>();

		@Override
		public boolean isSatisfiedBy(FileTreeElement fte) {
			return included.contains(fte.getFile());
		}

		@Override
		public void execute(InputFileDetails details) {
			included.add(details.getFile());
		}
		
		FileTree filter(FileTree toFilter) {
			return toFilter.matching(included.isEmpty() ? ACCEPT_NONE : new PatternSet().include(this)); 
		}
		
	}

}
