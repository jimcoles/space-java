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
 * @author Jim Coles
 */
public interface AssociationDecl extends Declartion {
    MetaType getMetaType();

    TypeRef getFromTypeRef();

    TypeRef getToTypeRef();

    DatumType getToType();

    @Override
    DatumType getType();

    MetaRefPart getFromPath();

    int getFromMult();

    MetaRefPart getToPath();

    int getToMult();

    boolean isRecursive();
}
