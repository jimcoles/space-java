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

import org.jkcsoft.space.lang.runtime.SpaceX;

import java.util.LinkedList;
import java.util.List;

/**
 * The central data type definition notion within Space.
 * Analogous to an OOP Class or Interface,
 * or an RDB Table definition, or an XML complex type.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class ComplexTypeImpl extends AbstractProjection implements ComplexType {

    private boolean isEntity;
    private List<Rule> equations;
    private List<TransformDefn> transformDefns;

    ComplexTypeImpl(SourceInfo sourceInfo, NamePart nameNode) {
        super(sourceInfo, nameNode.getText());
    }

    public boolean isComputed() {
        return false;
    }

    public boolean isEnumerated() {
        return true;
    }

    public List<Rule> getEquations() {
        return equations;
    }

    public List<TransformDefn> getTransformDefns() {
        return transformDefns;
    }

    @Override
    public boolean isAssignableTo(DatumType argsType) {
        return false;
    }

    /**
     * Might not be needed; essentially, asking 'does this type have a unique key'.
     */
    public boolean isEntity() {
        return isEntity;
    }

    @Override
    public ComplexType getRootType() {
        return null;
    }

}
