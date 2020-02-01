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

import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.Declaration;

/**
 * @author Jim Coles
 */
public interface Tuple extends Value {

    @Override
    DatumType getType();

    void initHolder(ValueHolder valueHolder);

    Tuple set(Declaration spaceDecl, Object javaObj);

    ValueHolder get(Declaration member);

    Reference getRefByOid(SpaceOid memberOid);

    @Override
    String toString();

    boolean isSingleWrapper();

}
