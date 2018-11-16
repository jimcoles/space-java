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

import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * Encapsulates a single part of a static reference path.
 *
 * A Namespace, Directory, Type, or Datum name. Namespaces, Directories, and Types.
 * Datums are value expressions; the other meta types are not.
 *
 * @author Jim Coles
 */
public class SimpleExprLink extends ExprLink {

    public SimpleExprLink(NamePartExpr linkOrRefExpr) {
        super(linkOrRefExpr.getSourceInfo(), linkOrRefExpr);
//        this.parentPath = parentPath;
    }

}
