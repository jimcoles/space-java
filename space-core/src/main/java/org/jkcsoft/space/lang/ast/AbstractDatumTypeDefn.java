/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
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
 * @author Jim Coles
 */
abstract public class AbstractDatumTypeDefn extends NamedElement implements DatumType {

    private SequenceTypeDefn sequenceTypeDefn;
    private SetTypeDefn setTypeDefn;

    protected AbstractDatumTypeDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    @Override
    public SequenceTypeDefn getSequenceOfType() {
        if (sequenceTypeDefn == null)
            sequenceTypeDefn = new SequenceTypeDefn(getSourceInfo(), this);
        return sequenceTypeDefn;
    }

    @Override
    public SetTypeDefn getSetOfType() {
        if (setTypeDefn == null)
            setTypeDefn = new SetTypeDefn(getSourceInfo(), this);
        return setTypeDefn;
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.TYPE;
    }
}
