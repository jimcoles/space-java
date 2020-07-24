/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.ast;

import java.util.*;

/**
 * The central data type definition notion within Space.
 * Analogous to an OOP Class or Interface,
 * or an RDB Table definition, or an XML complex type.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class TypeDefnImpl extends AbstractTypeDefn implements TypeDefn {

    private List<Rule> rules;
    private List<TransformDefn> transformDefns;
    private KeyDefnImpl primaryKey;
    private Set<KeyDefnImpl> alternateKeys = Collections.emptySet();
    // derived elements
    private ViewDefn pkViewDefn;
    private Set<ViewDefn> altKeyViewDefns;

    TypeDefnImpl(SourceInfo sourceInfo, NamePart nameNode) {
        super(sourceInfo, nameNode.getText());
    }

    TypeDefnImpl(SourceInfo sourceInfo, NamePart nameNode, boolean isView) {
        super(sourceInfo, nameNode.getText(), isView);
    }

    public boolean isComputed() {
        return false;
    }

    public boolean isEnumerated() {
        return true;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public List<TransformDefn> getTransformDefns() {
        return transformDefns;
    }

    @Override
    public boolean isAssignableTo(TypeDefn receivingType) {
        return false;
    }

    @Override
    public boolean hasPrimaryKey() {
        return primaryKey != null;
    }

    public void setPrimaryKey(KeyDefnImpl primaryKey) {
        this.primaryKey = primaryKey;
    }

    void setPkViewDefn(ViewDefn pkViewDefn) {
        this.pkViewDefn = pkViewDefn;
    }

    public void addAlternateKey(KeyDefnImpl keyDefn) {
        if (alternateKeys.isEmpty())
            alternateKeys = new TreeSet<>();
        alternateKeys.add(keyDefn);
    }

    void addAltKeyViewDefn(ViewDefn keyViewDefn) {
        altKeyViewDefns.add(keyViewDefn);
    }

    public void setAltKeyViewDefns(Set<ViewDefn> altKeyViewDefns) {
        this.altKeyViewDefns = altKeyViewDefns;
    }

    @Override
    public KeyDefnImpl getPrimaryKeyDefn() {
        return primaryKey;
    }

    @Override
    public Set<KeyDefnImpl> getAlternateKeyDefns() {
        return Set.of(primaryKey);
    }

    @Override
    public Comparator getTypeComparator() {
        return null;
    }

    public ViewDefn getPkViewDefn() {
        return pkViewDefn;
    }

    public Set<ViewDefn> getAltKeyViewDefns() {
        return altKeyViewDefns;
    }

}
