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
 * A general means of defining primary keys, alternate keys, and external keys.
 * Keys may be a simple a a single, opaque byte or as complex as tuple of scalar
 * variables.
 *
 * @author Jim Coles
 */
public class KeyDefn {

    private boolean isByteSeq;
    private int numBytes;

    private boolean isVarSet;
    private ViewDefn varSet;


}
