/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.Declaration;

import java.util.List;

/**
 * @author Jim Coles
 */
public interface DatumMap {

    void initHolder(ValueHolder valueHolder);

    DatumMap setValue(Declaration spaceDecl, Value value);

    DatumMap setValue(int idx, Value value);

    ValueHolder get(Declaration member);

    List<ValueHolder> getValueHolders();

    ValueHolder get(int idx);

    Object getJavaValue();
}
