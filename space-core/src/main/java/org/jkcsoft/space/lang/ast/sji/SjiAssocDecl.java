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
public class SjiAssocDecl extends NamedElement implements SjiDeclaration, AssociationDecl {

    protected SjiAssocDecl(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    @Override
    public TypeRef getFromTypeRef() {
        return null;
    }

    @Override
    public TypeRef getToTypeRef() {
        return null;
    }

    @Override
    public DatumType getToType() {
        return null;
    }

    @Override
    public DatumType getType() {
        return null;
    }

    @Override
    public MetaRefPart getFromPath() {
        return null;
    }

    @Override
    public int getFromMult() {
        return 0;
    }

    @Override
    public MetaRefPart getToPath() {
        return null;
    }

    @Override
    public int getToMult() {
        return 0;
    }

    @Override
    public boolean isRecursive() {
        return false;
    }

    @Override
    public MetaType getMetaType() {
        return null;
    }
}
