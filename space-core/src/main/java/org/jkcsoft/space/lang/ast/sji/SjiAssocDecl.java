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
public class SjiAssocDecl extends NamedElement implements SjiDeclaration, AssociationDefn {

    protected SjiAssocDecl(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
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
}
