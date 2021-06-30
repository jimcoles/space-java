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

import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * A datum ref in terms of a type ref part and the monomial datum name.
 * @author Jim Coles
 */
public class TwoPartDatumRef extends AbstractModelElement implements DatumRef {

    private final TypeRef typeRef;
    private final SimpleNameRefExpr<DatumDecl> monoDatumRef; // just the datum ref part
    //
    private final ExpressionChain<DatumDecl> fullDatumRef;

    public TwoPartDatumRef(SourceInfo sourceInfo, TypeRef typeRef, SimpleNameRefExpr<DatumDecl> monoDatumRef) {
        super(sourceInfo);
        this.typeRef = typeRef;
        this.monoDatumRef = monoDatumRef;
        fullDatumRef = AstFactory.getInstance().newMetaRefChain(SourceInfo.COMPOSITION, MetaType.DATUM, null);
        typeRef.getAllLinksAsHolders().forEach(fullDatumRef::addNextPart);
        fullDatumRef.addNextPart(monoDatumRef);
    }

    @Override
    public TypeDefn getTargetType() {
        return typeRef.getResolvedType();
    }

    @Override
    public DatumDecl getDatum() {
        return monoDatumRef.getResolvedMetaObj();
    }
}