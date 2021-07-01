/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast.sji;

import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * @author Jim Coles
 */
public abstract class SjiVarDecl extends AbstractNamedElement implements SjiDeclaration, VariableDecl, DatumRef {

    private SjiService sjiService;
    private SjiTypeDefn sjiTypeDefn;

    SjiVarDecl(SjiService sjiService, SourceInfo sourceInfo, SjiTypeDefn sjiTypeDefn, NamePart namePart) {
        super(sourceInfo, namePart);
        this.sjiService = sjiService;
        this.sjiTypeDefn = sjiTypeDefn;
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    public SjiTypeDefn getSjiTypeDefn() {
        return sjiTypeDefn;
    }

    @Override
    public DatumDeclContext getDeclContext() {
        return sjiTypeDefn;
    }

    @Override
    public TypeDefn getType() {
        return sjiTypeDefn;
    }

    SjiService getSjiService() {
        return sjiService;
    }

    @Override
    public boolean hasAssoc() {
        return false;
    }

    @Override
    public TypeDefn getTargetType() {
        return getType();
    }

    @Override
    public DatumDecl getDatum() {
        return this;
    }

    @Override
    public AssociationEnd getAssocEnd() {
        return null;
    }

    @Override
    public Comparators.DatumTupleComparator getDatumComparator() {
        return Comparators.buildDatumComparator(this);
    }
}
