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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Jim Coles
 */
public abstract class SjiVarDecl extends NamedElement implements SjiDeclaration, VariableDecl {

    private SjiTypeDefn sjiTypeDefn;

    SjiVarDecl(SourceInfo sourceInfo, SjiTypeDefn sjiTypeDefn, String name) {
        super(sourceInfo, name);
        this.sjiTypeDefn = sjiTypeDefn;
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.DATUM;
    }

    public SjiTypeDefn getSjiTypeDefn() {
        return sjiTypeDefn;
    }

}