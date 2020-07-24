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

import org.jkcsoft.space.lang.instance.Tuple;
import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.Comparator;

/**
 * A named set of Tuples of a certain type.  The 'name' is held via the extension of
 * {@link NamedElement}.
 *
 * @author Jim Coles
 */
public class SetDecl extends NamedElement implements Declaration {

    private ExpressionChain tupleTypeRef;

    SetDecl(SourceInfo sourceInfo, String name, ExpressionChain tupleTypeRef) {
        super(sourceInfo, name);

        this.tupleTypeRef = tupleTypeRef;
        //
        addChild(tupleTypeRef);
    }

    @Override
    public TypeDefn getType() {
        return ((TypeDefnImpl) tupleTypeRef.getResolvedMetaObj()).getSetOfType();
    }

    @Override
    public boolean isAssoc() {
        return false;
    }

    @Override
    public Comparator<Tuple> getDatumComparator() {
        return null;
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    public ExpressionChain getTupleTypeRef() {
        return tupleTypeRef;
    }

}
