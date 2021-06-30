/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2021 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * @author Jim Coles
 */
public abstract class AbstractAssocEnd extends AbstractModelElement implements AssociationEnd {

    private final DatumRef datumRef;
    private final AssociationDefn associationDefn;

    protected AbstractAssocEnd(SourceInfo sourceInfo, AssociationDefn associationDefn, DatumRef datumRef)
    {
        super(sourceInfo);
        this.associationDefn = associationDefn;
        this.datumRef = datumRef;
        //
        addChild(datumRef);
    }

    public AssociationDefn getAssociationDefn() {
        return associationDefn;
    }

    public TypeDefn getTargetType() {
        return datumRef.getTargetType();
    }

    @Override
    public TypeDefn getEndTargetType() {
        return datumRef.getTargetType();
    }

    @Override
    public DatumDecl getDatumDecl() {
        return datumRef.getDatum();
    }

}
