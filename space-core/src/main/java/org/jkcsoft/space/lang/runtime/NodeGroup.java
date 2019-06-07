/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2019 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.ast.ModelElement;
import org.jkcsoft.space.lang.ast.Named;

import java.util.SortedSet;

/**
 * For gropuing named nodes by context, e.g., group variables by local,
 *
 * @author Jim Coles
 */
public class NodeGroup {

    private String groupName;
    private Named namedElement;

}
