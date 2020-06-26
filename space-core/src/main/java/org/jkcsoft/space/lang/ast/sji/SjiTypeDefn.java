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

import java.util.Set;

/**
 * Wraps Java Class to implement an AST Type Defn.
 *
 * @author Jim Coles
 */
public class SjiTypeDefn extends AbstractTypeDefn implements TypeDefn {

    // wrapped element
    private Class jClass;
    private KeyDefn primaryKey;
    private Set<KeyDefn> allKeys;

    SjiTypeDefn(Class jClass) {
        super(new NativeSourceInfo(jClass), jClass == null ? "(method args)" : jClass.getSimpleName());
        this.jClass = jClass;
    }

    @Override
    public MetaType getMetaType() {
        return super.getMetaType();
    }

//    @Override
//    public int getScalarDofs() {
//        return datumDecls.size();
//    }

    @Override
    public boolean isPrimitiveType() {
        return false;
    }

    @Override
    public KeyDefn getPrimaryKeyDefn() {
        return primaryKey;
    }

    @Override
    public Set<KeyDefn> getAllKeyDefns() {
        return allKeys;
    }

    @Override
    public boolean isAssignableTo(TypeDefn receivingType) {
        return false;
    }

}
