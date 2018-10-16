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

import org.jkcsoft.space.lang.instance.SetSpace;

import java.util.List;

/**
 * Represents our intrinsic notion of a Query against a Space (set
 * of Types, Equations, and related).
 *
 * @author Jim Coles
 * @version 1.0
 */
public class QueryDefn extends EquationDefn {

    private SetSpace rootSpace;
    private List<NamePartExpr> variables;
    private OperatorExpr filter;  // nestable expression

    public QueryDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

}
