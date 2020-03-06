/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import java.util.List;

/**
 * The primary {@link TreeView} implementation.
 *
 * @author Jim Coles
 */
public class TreeViewImpl implements TreeView {

    private ExpressionChain rootTypeRefExpr;
    private Rule rootNodeCondition;
    private List<AliasedMetaRef> typeAssocs;

}
