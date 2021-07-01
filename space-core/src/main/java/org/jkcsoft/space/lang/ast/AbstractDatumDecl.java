/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2021 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * @author Jim Coles
 */
public abstract class AbstractDatumDecl extends AbstractNamedElement implements DatumDecl {

    private DatumDeclContext declContext;   // this datum's parent container: a Type or a Statement Block
    private TypeRef typeRef;    // this datum's type

    protected AbstractDatumDecl(SourceInfo sourceInfo, DatumDeclContext declContext, NamePart namePart, TypeRef typeRef) {
        super(sourceInfo, namePart);
        this.declContext = declContext;
        this.typeRef = typeRef;
        //
        addChild(typeRef);
    }

    @Override
    public TypeDefn getType() {
        return typeRef.getResolvedType();
    }

    public void setType(TypeRef typeRef) {
        this.typeRef = typeRef;
    }

    @Override
    public DatumDeclContext getDeclContext() {
        return declContext;
    }

}
