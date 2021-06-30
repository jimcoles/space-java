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

import java.util.LinkedList;

/**
 * @author Jim Coles
 */
public class TypeDatumRef extends AbstractModelElement implements DatumRef {

    private final ExpressionChain<DatumDecl> fullDatumRef;

    public TypeDatumRef(SourceInfo sourceInfo, ExpressionChain<DatumDecl> fullDatumRef) {
        super(sourceInfo);
        this.fullDatumRef = fullDatumRef;
    }

    @Override
    public TypeDefn getTargetType() {
        LinkedList<NameRefOrHolder> links = fullDatumRef.getAllLinksAsHolders();
        // 2nd to last element is the type def
        return ((TypeDefn) links.get(links.size() - 2).getRefAsNameRef().getResolvedMetaObj());
    }

    @Override
    public DatumDecl getDatum() {
        return fullDatumRef.getResolvedMetaObj();
    }

}
