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
 * A named set of Tuples of a certain type.  The 'name' is held via the extension of
 * {@link AbstractNamedElement}.
 *
 * @author Jim Coles
 */
public class SetDecl extends AbstractDatumDecl {

    private TypeRef tupleTypeRef;

    SetDecl(SourceInfo sourceInfo, DatumDeclContext datumDeclContext, NamePart namePart, TypeRef tupleTypeRef) {
        super(sourceInfo, datumDeclContext, namePart, tupleTypeRef);

        this.tupleTypeRef = tupleTypeRef;
        //
        addChild(tupleTypeRef);
    }

    @Override
    public TypeDefn getType() {
        return ((TypeDefnImpl) tupleTypeRef.getResolvedType()).getSetOfType();
    }

    @Override
    public boolean hasAssoc() {
        return false;
    }

    @Override
    public Comparators.DatumTupleComparator getDatumComparator() {
        return null;
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    public TypeRef getTupleTypeRef() {
        return tupleTypeRef;
    }

}
