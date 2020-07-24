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

import org.jkcsoft.space.lang.ast.NamedElement;
import org.jkcsoft.space.lang.ast.SourceInfo;
import org.jkcsoft.space.lang.ast.TypeDefn;
import org.jkcsoft.space.lang.ast.VariableDecl;
import org.jkcsoft.space.lang.instance.Tuple;
import org.jkcsoft.space.lang.metameta.MetaType;
import org.jkcsoft.space.lang.runtime.AstUtils;

import java.util.Comparator;

/**
 * @author Jim Coles
 */
public abstract class SjiVarDecl extends NamedElement implements SjiDeclaration, VariableDecl {

    private SjiService sjiService;
    private SjiTypeDefn sjiTypeDefn;

    SjiVarDecl(SjiService sjiService, SourceInfo sourceInfo, SjiTypeDefn sjiTypeDefn, String name) {
        super(sourceInfo, name);
        this.sjiService = sjiService;
        this.sjiTypeDefn = sjiTypeDefn;
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    public SjiTypeDefn getSjiTypeDefn() {
        return sjiTypeDefn;
    }

    SjiService getSjiService() {
        return sjiService;
    }

    @Override
    public TypeDefn getType() {
        return sjiTypeDefn;
    }

    @Override
    public boolean isAssoc() {
        return false;
    }

    @Override
    public Comparator<Tuple> getDatumComparator() {
        return AstUtils.buildDatumComparator(this);
    }
}
