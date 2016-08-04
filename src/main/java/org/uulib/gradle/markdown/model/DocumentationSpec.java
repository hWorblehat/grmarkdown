package org.uulib.gradle.markdown.model;

import org.gradle.model.Managed;
import org.gradle.platform.base.ComponentSpec;
import org.gradle.platform.base.GeneralComponentSpec;

/**
 * A {@linkplain ComponentSpec} for a documentation component.
 * @author Rowan Lonsdale
 */
@Managed
public interface DocumentationSpec extends GeneralComponentSpec {}
