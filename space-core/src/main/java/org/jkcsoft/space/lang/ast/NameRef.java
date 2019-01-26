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

/**
 * An expression that is or contains a reference-by-name to a named thing
 * such as a datum or function.
 *
 * @author Jim Coles
 */
public interface NameRef<T extends Named> {

    void setState(LinkState resolved);

    boolean isResolved();

    void setResolvedMetaObj(T resolvedMetaObj);

    Named getResolvedMetaObj();

    void setTypeCheckState(TypeCheckState typeCheckState);

    String toUrlString();

    boolean isWildcard();
}
