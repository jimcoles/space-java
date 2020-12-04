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

/**
 * Essentially a {@link TypeRefImpl} with an optional alias.
 * @author Jim Coles
 */
public class ImportExpr extends AbstractModelElement {

    private TypeRefImpl typeRefExpr;
    private String alias;

    public ImportExpr(SourceInfo sourceInfo, TypeRefImpl typeRefExpr, String alias) {
        super(sourceInfo);
        this.typeRefExpr = typeRefExpr;
        this.alias = alias;
        //
        addChild(typeRefExpr);
    }

    public TypeRefImpl getTypeRefExpr() {
        return typeRefExpr;
    }

    public String getAlias() {
        return alias;
    }
}
