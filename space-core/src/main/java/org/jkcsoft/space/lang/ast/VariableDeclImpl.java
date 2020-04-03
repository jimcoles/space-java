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
 * Meta-level element of a VariableValueHolder. Declares a named 'usage' of
 * a Primitive by a Type.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class VariableDeclImpl extends NamedElement implements VariableDecl {

    private TypeRef typeRef;

    VariableDeclImpl(SourceInfo sourceInfo, String name, TypeRef typeRef) {
        super(sourceInfo, name);
        this.typeRef = typeRef;
        //
        addChild(((AbstractModelElement) typeRef));
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    @Override
    public NumPrimitiveTypeDefn getType() {
        return ((NumPrimitiveTypeDefn) typeRef.getResolvedType());
    }

    public void setType(TypeRef typeRef) {
        this.typeRef = typeRef;
    }

}
