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

/**
 * A {@link DomainDefn} is a wrapper around a 1-dimensional primitive type.
 * A Domain superimposes constraints on simple primitive. Domains may also
 * allow of different kind of polymorphism, e.g., certain functions may be
 * applicable to any objects that contain a variable of a certain Domain
 * regardless of the objects type.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class DomainDefn extends NamedElement implements SimpleType {

    private SequenceTypeDefn sequenceTypeDefn;

    DomainDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
        sequenceTypeDefn = new SequenceTypeDefn(getSourceInfo(), this);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.TYPE;
    }

    @Override
    public int getScalarDofs() {
        return 1;
    }

    @Override
    public SequenceTypeDefn getSequenceOfType() {
        return sequenceTypeDefn;
    }

    @Override
    public boolean isPrimitiveType() {
        return false;
    }

    @Override
    public boolean isAssignableTo(DatumType argsType) {
        return false;
    }

}
