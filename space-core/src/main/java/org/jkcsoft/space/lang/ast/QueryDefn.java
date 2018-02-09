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

import org.jkcsoft.space.lang.instance.Space;

import java.util.List;

/**
 * Represents our intrinsic notion of a query against the set of Space type definitions,
 * objects and spaces.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class QueryDefn extends EquationDefn {

    private Space rootSpace;
    private List<SpacePathExpr> variables;
    private BooleanExpr filter;  // nestable expression

    QueryDefn(SpaceTypeDefn contextSpaceTypeDefn, String name) {

    }

}
