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
    public TypeDefn getToType() {
        return null;
    }

    @Override
    public TypeDefn getType() {
        return null;
    }

    @Override
    public AssociationDefnEnd getFromEnd() {
        return null;
    }

    @Override
    public AssociationDefnEnd getToEnd() {
        return null;
    }

    @Override
    public boolean isRecursive() {
        return false;
    }

    @Override
    public MetaType getMetaType() {
        return null;
    }

    protected SjiService getSjiService() {
        return sjiService;
    }
}
