package org.uulib.grmd;

import com.google.common.collect.ImmutableList;

public interface Defaults {

	String MARKDOWN_TASK_GROUP = "Documentation";
	String HTML_FILE_EXTENSION = "html";
	ImmutableList<String> MARKDOWN_FILE_EXTENSIONS = ImmutableList.of("markdown", "md");

}