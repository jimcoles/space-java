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
 * A instance of this type is created for every occurrence of a literal string
 * in a source file.
 *
 * @author Jim Coles
 */
public class SequenceLiteralExpr extends ModelElement implements ValueExpr {

    private MetaReference<StreamTypeDefn> typeRef;
    private String valueExpr;

    SequenceLiteralExpr(SourceInfo sourceInfo, SpacePathExpr pathExpr, String valueExpr) {
        super(sourceInfo);
        this.typeRef = new MetaReference<StreamTypeDefn>(pathExpr, MetaType.TYPE);
        this.valueExpr = valueExpr;
        //
        addChild(typeRef);
    }

    public MetaReference<StreamTypeDefn> getTypeRef() {
        return typeRef;
    }

    public String getValueExpr() {
        return valueExpr;
    }

    @Override
    public String getText() {
        return valueExpr;
    }
}
