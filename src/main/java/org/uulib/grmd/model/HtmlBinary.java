package org.uulib.grmd.model;

import java.io.File;

import org.gradle.model.Managed;
import org.gradle.platform.base.BinarySpec;

/**
 * A generated directory structure of HTML files.
 * @author Rowan Lonsdale
 */
@Managed
public interface HtmlBinary extends BinarySpec {
	
	/**
	 * @return The directory into which this binary will be generated.
	 */
	File getOutputDir();
	
	/**
	 * Sets the directory into which this binary will be generated.
	 * @param outputDir The output directory to use.
	 */
	void setOutputDir(File outputDir);
	
	/**
	 * @return The file extension to use for generated HTML files.
	 */
	String getHtmlFileExtension();
	
	/**
	 * Sets the file extension to use for generated HTML files.
	 * @param extension The file extension to use.
	 */
	void setHtmlFileExtension(String extension);

}
