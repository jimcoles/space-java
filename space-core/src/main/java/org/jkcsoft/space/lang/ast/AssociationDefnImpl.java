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
 * The primary {@link AssociationDefn} implementation.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class AssociationDefnImpl extends AbstractNamedElement implements AssociationDefn {

    private FromAssocEnd fromEnd;
    private ToAssocEnd toEnd;
    private AssociationKind associationKind;

    AssociationDefnImpl(SourceInfo sourceInfo, NamePart namePart)
    {
        super(sourceInfo, namePart);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    public void setFromEnd(FromAssocEnd fromEnd) {
        this.fromEnd = fromEnd;

        // child adders
        if (this.fromEnd != null) {
            addChild(this.fromEnd);
        }
    }

    public void setToEnd(ToAssocEnd toEnd) {
        this.toEnd = toEnd;

        // child adders
        addChild(this.toEnd);
    }

    @Override
    public AssociationDefn setAssociationKind(AssociationKind associationKind) {
        this.associationKind = associationKind;
        return this;
    }

    @Override
    public AssociationKind getAssociationKind() {
        return associationKind;
    }

    @Override
    public boolean hasFromEnd() {
        return fromEnd != null;
    }

    @Override
    public FromAssocEnd getFromEnd() {
        return fromEnd;
    }

    @Override
    public TypeDefn getFromType() {
        return fromEnd.getEndTargetType();
    }

    @Override
    public ToAssocEnd getToEnd() {
        return toEnd;
    }

    @Override
    public TypeDefn getToType() {
        return toEnd.getEndTargetType();
    }

    /**
     * In Space, this expression would go in an Equation expression.
     */
    @Override
    public boolean isRecursive() {
        return fromEnd.getEndTargetType() == toEnd.getEndTargetType();
    }

}
