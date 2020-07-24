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
import org.jkcsoft.space.lang.instance.Tuple;
import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.Comparator;

/**
 * @author Jim Coles
 */
public abstract class SjiAssocDecl extends NamedElement implements SjiDeclaration, AssociationDefn {

    private SjiService sjiService;
    private SjiAssociationDefnEnd fromEnd;
    private SjiAssociationDefnEnd toEnd;

    protected SjiAssocDecl(SjiService sjiService, SourceInfo sourceInfo, SjiTypeDefn fromType, SjiTypeDefn toType, String name) {
        super(sourceInfo, name);

        this.sjiService = sjiService;
        if (fromType != null) {
            this.fromEnd = new SjiAssociationDefnEnd(sourceInfo, name, fromType, 1, 1);
            addChild(this.fromEnd);
        }

        if (toType == null) throw new RuntimeException("bug: path to class ref cannot be null");
        this.toEnd = new SjiAssociationDefnEnd(sourceInfo, name, toType, 1, 1);
        addChild(this.toEnd);
    }

    @Override
    public boolean isAssoc() {
        return true;
    }

    @Override
    public TypeDefn getToType() {
        return toEnd.getType();
    }

    @Override
    public TypeDefn getType() {
        return toEnd.getType();
    }

    @Override
    public AssociationDefnEnd getFromEnd() {
        return fromEnd;
    }

    @Override
    public AssociationDefnEnd getToEnd() {
        return toEnd;
    }

    @Override
    public boolean isRecursive() {
        return toEnd == fromEnd;
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    protected SjiService getSjiService() {
        return sjiService;
    }

    @Override
    public Comparator<Tuple> getDatumComparator() {
        return getType().getTypeComparator();
    }
}
