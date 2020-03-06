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
 * NOT USED. See {@link View} instead.
 *
 * Represents our intrinsic notion of a QueryImpl (definition) against an Object Space
 * (sets of Tuples).
 *
 * @author Jim Coles
 * @version 1.0
 */
public class QueryImpl extends NamedElement implements Query {

    protected QueryImpl(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    @Override
    public MetaType getMetaType() {
        return null;
    }
}
