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
public class AssociationDefnImpl extends NamedElement implements AssociationDefn {

    private AssociationDefnEnd fromEnd;
    private AssociationDefnEnd toEnd;

    AssociationDefnImpl(SourceInfo sourceInfo, String name, TypeRef fromTypeRef, TypeRef toTypeRef) {
        super(sourceInfo, name);

        if (fromTypeRef != null) {
            this.fromEnd = new AssociationDefnEndImpl(sourceInfo, name, fromTypeRef, 1, 1);
            addChild(this.fromEnd);
        }

        if (toTypeRef == null) throw new RuntimeException("bug: path to class ref cannot be null");
        this.toEnd = new AssociationDefnEndImpl(sourceInfo, name, toTypeRef, 1, 1);
        addChild(this.toEnd);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    @Override
    public DatumType getToType() {
        return toEnd.getType();
    }

    @Override
    public DatumType getType() {
        return getToType();
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

}
