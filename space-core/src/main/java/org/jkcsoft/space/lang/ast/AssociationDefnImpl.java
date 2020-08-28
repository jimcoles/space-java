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
 * The primary {@link AssociationDefn} implementation.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class AssociationDefnImpl extends NamedElement implements AssociationDefn {

    private AssociationDefnEnd fromEnd;
    private AssociationDefnEnd toEnd;
    private AssociationKind associationKind;

    AssociationDefnImpl(SourceInfo sourceInfo, String name, TypeRef fromTypeRef, TypeRef toTypeRef) {
        super(sourceInfo, name);

        if (fromTypeRef != null) {
            this.fromEnd = new AssociationDefnEndImpl(sourceInfo, name, fromTypeRef, true, true, 1, 1);
        }

        if (toTypeRef == null) throw new RuntimeException("bug: path to class ref cannot be null");
        setToEnd(sourceInfo, name, toTypeRef);

        // child adders
        if (this.fromEnd != null) {
            addChild(this.fromEnd);
        }
        addChild(this.toEnd);
    }

    private void setToEnd(SourceInfo sourceInfo, String name, TypeRef toTypeRef) {
        this.toEnd = new AssociationDefnEndImpl(sourceInfo, name, toTypeRef, true, true, 1, 1);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    @Override
    public boolean isAssoc() {
        return true;
    }

    @Override
    public Comparator<Tuple> getDatumComparator() {
        return null;
    }

    @Override
    public TypeDefn getToType() {
        return toEnd.getType();
    }

    @Override
    public TypeDefn getType() {
        return getToType();
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
    public AssociationDefnEnd getFromEnd() {
        return fromEnd;
    }

    @Override
    public AssociationDefnEnd getToEnd() {
        return toEnd;
    }

    /**
     * In Space, this expression would go in an Equation expression.
     */
    @Override
    public boolean isRecursive() {
        return fromEnd.getType() == toEnd.getType();
    }

    // called by loader after fully loaded to set


}
