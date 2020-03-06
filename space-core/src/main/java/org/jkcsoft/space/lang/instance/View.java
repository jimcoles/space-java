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

import org.jkcsoft.space.lang.ast.QueryImpl;

/**
 * @author Jim Coles
 * @version 1.0
 */
public class View {

    private QueryImpl query;

    View() {

    }

    /** The computed normalization.  Basis Spaces are
     * 3rd normal form.  Computed Views are general something
     * else. */
    public int getNormalForm() {
        return -1;
    }

    /** The computed Degrees of Freedom (DOF) of this
     * view, which is determined based on the joins from the
     * base Space Definition. */
    public int getDof() {
        return -1;
    }

}
