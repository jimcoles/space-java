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
 * @author Jim Coles
 */
public class VoidType extends NamedElement implements DatumType {

    public static final VoidType VOID = new VoidType(new IntrinsicSourceInfo(), "void");

    private VoidType(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    @Override
    public int getScalarDofs() {
        return 0;
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.TYPE;
    }
}
