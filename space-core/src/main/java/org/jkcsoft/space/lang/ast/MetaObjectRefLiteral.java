/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.ast;

/**
 * Hold reference to known intrinsic objects such as operator functions.
 *
 * @author Jim Coles
 */
public class MetaObjectRefLiteral implements ValueExpr {

    private ModelElement spaceMetaObject;

    public MetaObjectRefLiteral(ModelElement spaceMetaObject) {
        this.spaceMetaObject = spaceMetaObject;
    }

    public ModelElement getSpaceMetaObject() {
        return spaceMetaObject;
    }
}
