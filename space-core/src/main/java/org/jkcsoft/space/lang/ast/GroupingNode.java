/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Indicates that the AST class is a grouping mechanism and therefore it should inherit
 * it's parent's namespace, i.e., as if the group node did not exist and the group's
 * children existed directly in the parent.
 *
 * @author Jim Coles
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface GroupingNode {

}
