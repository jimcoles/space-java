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

import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.List;

/**
 * @author Jim Coles
 */
public abstract class AbstractCollectionTypeDefn extends NamedElement implements CollectionType {

    private DatumType containedElementType;
    private SequenceTypeDefn sequenceTypeDefn;

    protected AbstractCollectionTypeDefn(SourceInfo sourceInfo,
                                         String name,
                                         DatumType containedElementType) {
        super(sourceInfo, name);
        this.containedElementType = containedElementType;
    }

    public DatumType getContainedElementType() {
        return containedElementType;
    }

    @Override
    public int getScalarDofs() {
        return containedElementType.getScalarDofs();
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.TYPE;
    }

    @Override
    public boolean isNamed() {
        return containedElementType.isNamed();
    }

    @Override
    public boolean isPrimitiveType() {
        return containedElementType instanceof NumPrimitiveTypeDefn;
    }

    @Override
    public boolean isSimpleType() {
        return containedElementType.isSimpleType();
    }

    /** True if this set hold references to objects; false if it hold values of primitives */
    public boolean isReferenceType() {
        return containedElementType instanceof SpaceTypeDefn;
    }

    @Override
    public List<String> getFullNamePath() {
        return null;
    }

    @Override
    public SequenceTypeDefn getSequenceOfType() {
        if (sequenceTypeDefn == null)
            sequenceTypeDefn = new SequenceTypeDefn(getSourceInfo(), this);
        return sequenceTypeDefn;
    }

    @Override
    public boolean isAssignableTo(DatumType argsType) {
        return false;
    }
}
