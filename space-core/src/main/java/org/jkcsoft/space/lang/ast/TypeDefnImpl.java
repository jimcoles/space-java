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

import java.util.List;
import java.util.Set;

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
    private KeyDefn primaryKey;

    TypeDefnImpl(SourceInfo sourceInfo, NamePart nameNode) {
        super(sourceInfo, nameNode.getText());
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

    public void setPrimaryKey(KeyDefn primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public KeyDefn getPrimaryKeyDefn() {
        return primaryKey;
    }

    @Override
    public Set<KeyDefn> getAllKeyDefns() {
        return Set.of(primaryKey);
    }
}
