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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * Wraps Java Class to implement an AST Type Defn.
 *
 * @author Jim Coles
 */
public class SjiTypeDefn extends AbstractTypeDefn implements TypeDefn {

    private SjiService sjiService;
    // wrapped element
    private Class jClass;

    SjiTypeDefn(SjiService sjiService, Class jClass) {
        super(new NativeSourceInfo(jClass), sjiService.newSjiNamePart(jClass, jClass.getSimpleName()));
        this.sjiService = sjiService;
        this.jClass = jClass;
    }

    /** Method arg pseudo-type. */
    SjiTypeDefn(SjiService sjiService, Method jMethod) {
        super(new NativeSourceInfo(jMethod), sjiService.newSjiNamePart(jMethod, "(method args)"));
        this.sjiService = sjiService;
    }

    @Override
    public MetaType getMetaType() {
        return super.getMetaType();
    }

    @Override
    public boolean isSjiWrapper() {
        return true;
    }

    //    @Override
//    public int getScalarDofs() {
//        return datumDecls.size();
//    }

    public Class getjClass() {
        return jClass;
    }

    @Override
    public boolean isPrimitive() {
        return false;
    }

    @Override
    public KeyDefnImpl getPrimaryKeyDefn() {
        return null;
    }

    @Override
    public Set<KeyDefnImpl> getAlternateKeyDefns() {
        return Collections.EMPTY_SET;
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
