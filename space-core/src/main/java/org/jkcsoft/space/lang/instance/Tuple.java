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
import org.jkcsoft.space.lang.ast.Declartion;
import org.jkcsoft.space.lang.ast.Named;

/**
 * @author Jim Coles
 */
public interface Tuple extends Value {
    @Override
    DatumType getType();

    void initHolder(ValueHolder valueHolder);

    ValueHolder get(Declartion member);

    Reference getRefByOid(SpaceOid memberOid);

    @Override
    String toString();
}
