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

import org.jkcsoft.space.lang.ast.sji.SjiVarDecl;
import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * The implementation for {@link VariableDecl} which is a specialization of
 * {@link DatumDecl}.
 *
 * @see DatumProjectionImpl
 * @see SjiVarDecl
 * @author Jim Coles
 * @version 1.0
 */
public class VariableDeclImpl extends AbstractDatumDecl implements VariableDecl {

    private FromAssocEnd assocEnd;
    private Comparators.DatumTupleComparator datumComparator;

    VariableDeclImpl(SourceInfo sourceInfo, DatumDeclContext declContext, NamePart namePart, TypeRef datumTypeRef) {
        super(sourceInfo, declContext, namePart, datumTypeRef);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    public void setAssocEnd(FromAssocEnd assocEnd) {
        this.assocEnd = assocEnd;
    }

    @Override
    public AssociationEnd getAssocEnd() {
        return assocEnd;
    }

    @Override
    public boolean hasAssoc() {
        return assocEnd != null;
    }

    @Override
    public Comparators.DatumTupleComparator getDatumComparator() {
        if (datumComparator == null)
            datumComparator = Comparators.buildDatumComparator(this);
        return datumComparator;
    }

}
