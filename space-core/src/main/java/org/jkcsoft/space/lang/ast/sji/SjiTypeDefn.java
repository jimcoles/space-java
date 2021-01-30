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
import org.jkcsoft.space.lang.instance.ScalarValue;
import org.jkcsoft.space.lang.instance.Tuple;
import org.jkcsoft.space.lang.metameta.MetaType;
import org.jkcsoft.space.lang.runtime.SpaceUtils;

import java.util.Comparator;
import java.util.Set;

/**
 * Wraps Java Class to implement an AST Type Defn.
 *
 * @author Jim Coles
 */
public class SjiTypeDefn extends AbstractTypeDefn implements TypeDefn {

    // wrapped element
    private Class jClass;
    private KeyDefnImpl primaryKey;
    private Set<KeyDefnImpl> allKeys;

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
    public KeyDefnImpl getPrimaryKeyDefn() {
        return primaryKey;
    }

    @Override
    public Set<KeyDefnImpl> getAlternateKeyDefns() {
        return allKeys;
    }

    @Override
    public Comparators.ProjectionComparator getTypeComparator() {
        throw SpaceUtils.nosup("getTypeComparator");
    }

    @Override
    public Comparator<ScalarValue> getValueComparator() {
        throw SpaceUtils.nosup("getValueComparator");
    }

    @Override
    public boolean isAssignableTo(TypeDefn receivingType) {
        return false;
    }

    @Override
    public boolean hasPrimaryKey() {
        return false;
    }

}
