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
import org.jkcsoft.space.lang.runtime.SpaceUtils;

/**
 * @author Jim Coles
 */
public abstract class SjiAssocDecl extends NamedElement implements SjiDeclaration, AssociationDefn {

    private SjiService sjiService;
    private SjiFromAssocEnd fromEnd;
    private SjiToAssocEnd toEnd;
    private AssociationKind associationKind;

    protected SjiAssocDecl(SjiService sjiService, SourceInfo sourceInfo, SjiVarDecl fromRef,
                           SjiVarDecl toRef, NamePart namePart) {
        super(sourceInfo, namePart);

        this.sjiService = sjiService;
        if (fromRef != null) {
            this.fromEnd = new SjiFromAssocEnd(sourceInfo, this, fromRef);
            addChild(this.fromEnd);
        }

        if (toRef == null) throw new RuntimeException("bug: path to class ref cannot be null");

        this.toEnd = new SjiToAssocEnd(sourceInfo, this, toRef);
        addChild(this.toEnd);
    }

    @Override
    public boolean hasAssoc() {
        return true;
    }

    @Override
    public FromAssocEnd getFromEnd() {
        return null;
    }

    @Override
    public boolean hasFromEnd() {
        return false;
    }

    @Override
    public TypeDefn getToType() {
        return toEnd.getEndTargetType();
    }

    @Override
    public TypeDefn getType() {
        return toEnd.getEndTargetType();
    }

    @Override
    public ToAssocEnd getToEnd() {
        return toEnd;
    }

    @Override
    public boolean isRecursive() {
        return toEnd.getEndTargetType() == fromEnd.getEndTargetType();
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    @Override
    public AssociationKind getAssociationKind() {
        return associationKind;
    }

    @Override
    public AssociationDefn setAssociationKind(AssociationKind kind) {
        this.associationKind = kind;
        return this;
    }

    @Override
    public Comparators.DatumTupleComparator getDatumComparator() {
        throw SpaceUtils.nosup("getDatumComparator");
    }

    protected SjiService getSjiService() {
        return sjiService;
    }

}
