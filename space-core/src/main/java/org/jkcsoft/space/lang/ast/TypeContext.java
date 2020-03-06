/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.List;

/**
 * Used for notational convenience when specifying projections of a
 * set of {@link ComplexType}'s.
 *
 * @author Jim Coles
 */
public class TypeContext extends NamedElement {

    private TypeRef rootTypeRef;
    private List<ExpressionChain> varPathRefs;

    protected TypeContext(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    @Override
    public MetaType getMetaType() {
        return null;
    }

    void addRootTypeRef(TypeRef rootTypeRef) {
        this.rootTypeRef = rootTypeRef;
        //
        addChild(rootTypeRef);
    }

    void addVarRef(ExpressionChain varRef) {
        varPathRefs.add(varRef);
        //
        addChild(varRef);
    }

}
