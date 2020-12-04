/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.ContextDatumDefn;
import org.jkcsoft.space.lang.ast.Declaration;

import java.util.*;

/**
 * @author Jim Coles
 */
public class BlockDatumMap implements DatumMap {

    private final ContextDatumDefn defn;
    private final Map<Declaration, ValueHolder<Value<Object>, Object>> valueHolders;

    protected BlockDatumMap(ContextDatumDefn defn) {
        this.defn = defn;
        this.valueHolders = new TreeMap<>();
    }

    @Override
    public void initHolder(ValueHolder valueHolder) {
        valueHolders.put(valueHolder.getDeclaration(), valueHolder);
    }

    @Override
    public DatumMap setValue(Declaration spaceDecl, Value value) {
        get(spaceDecl).setValue(value);
        return this;
    }

    @Override
    public DatumMap setValue(int idx, Value value) {
        return this;
    }

    private ObjectFactory getObjectFactory() {
        return ObjectFactory.getInstance();
    }

    @Override
    public ValueHolder get(Declaration member) {
        return valueHolders.get(member);
    }

    public Declaration getDeclAt(int idx) {
        // no-op
        return null;
    }

    @Override
    public List<ValueHolder> getValueHolders() {
        return new LinkedList<>(valueHolders.values());
    }

    @Override
    public ValueHolder get(int idx) {
        return null;
    }

    public int getSize() {
        return valueHolders.size();
    }

    @Override
    public Object getJavaValue() {
        return null;
    }

}
