/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.Declaration;
import org.jkcsoft.space.lang.ast.ProjectionDecl;
import org.jkcsoft.space.lang.ast.TypeDefn;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 *
 * @author Jim Coles
 * @version 1.0
 */
public abstract class AbstractTuple extends AbstractSpaceObject implements Tuple, ExeContext {

    private byte[] creatorId;
    private final ValueHolder[] valueHolders;
//    private List<ValueHolder> valueHolders = new LinkedList<>();
    // seminal map
//    private Map<SpaceOid, ValueHolder> indexAllByMemberOid = new HashMap<>();

    protected AbstractTuple(SpaceOid oid, TypeDefn defn) {
        super(oid, defn);
        valueHolders = new ValueHolder[defn.getScalarDofs()];
    }

    @Override
    public ProjectionDecl getType() {
        return (ProjectionDecl) getDefn();
    }

    @Override
    public boolean isSingleValueWrapper() {
        return valueHolders.length == 1;
    }

    @Override
    public void initHolder(ValueHolder valueHolder) {
        int idxHolder;
        if (getDefn() instanceof TypeDefn) {
            idxHolder = getIdxHolder(valueHolder.getDeclaration());
        }
        else {
            idxHolder = 0;
        }
        valueHolders[idxHolder] = valueHolder;
    }

    @Override
    public Tuple setValue(Declaration spaceDecl, Value value) {
        get(spaceDecl).setValue(value);
        return this;
    }

    @Override
    public Tuple setValue(int idx, Value value) {
        valueHolders[idx].setValue(value);
        return this;
    }

    @Override
    public ValueHolder get(Declaration member) {
        return valueHolders[getIdxHolder(member)];
    }

    public Declaration getDeclAt(int idx) {
//        return ((TypeDefn) getDefn()).getDatumDeclList().get(idx);
        return valueHolders[idx].getDeclaration();
    }

    @Override
    public List<ValueHolder> getValueHolders() {
        return Arrays.asList(valueHolders);
    }

    @Override
    public ValueHolder get(int idx) {
        return valueHolders[idx];
    }

    public int getSize() {
        return valueHolders.length;
    }

    @Override
    public Object getJavaValue() {
        Object[] values = new Object[getSize()];
        for (int idxHolder = 0; idxHolder < valueHolders.length; idxHolder++) {
            values[idxHolder] = valueHolders[idxHolder].getValue().getJavaValue();
        }
        return values;
    }

    private int getIdxHolder(Declaration datumDecl) {
        return ((TypeDefn) getDefn()).getDatumDecls().indexOf(datumDecl);
    }

    /** Get the 0-based ordinal of the specified member */
    private int getMemberIdx(SpaceOid memberOid) {
        int idxMember = -1;
        List<Declaration> allMembers = ((TypeDefn) getDefn()).getDatumDecls();
        for (int idx = 0; idx < allMembers.size(); idx++) {
            if (allMembers.get(idx).getOid().equals(memberOid)) {
                idxMember = idx;
                break;
            }
        }
        return idxMember;
    }

    @Override
    public ValueCollection<ValueHolder> addValue(ValueHolder holder) {
        return this;
    }

    @Override
    public void forEach(Consumer<? super ValueHolder> action) {
        for (ValueHolder valueHolder : valueHolders) {
            action.accept(valueHolder);
        }
    }

    @Override
    public Spliterator<ValueHolder> spliterator() {
        return Arrays.spliterator(valueHolders);
    }

    @Override
    public Iterator<ValueHolder> iterator() {
        return Arrays.asList(valueHolders).iterator();
    }

    @Override
    public String toString() {
        return "([" + super.toString() + "] "
            + Strings.buildCommaDelList(getValueHolders(), obj -> ((ValueHolder) obj).getValue().toString())
            + ")";
    }

}