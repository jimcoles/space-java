/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast.persist;

import org.jkcsoft.space.lang.ast.ProjectionDecl;
import org.jkcsoft.space.lang.instance.*;

import java.util.List;

/**
 * Visits every Space node writes out to a simple text format that looks
 * a bit like JSON or GPB text format.
 *
 * @author Jim Coles
 */
public class SponWriter {

    private Space space;
    private ProjectionDecl treeProjection;

    public void writeSpon(ReferenceValue indexRef) {
        List<View> allIndices = space.getViews();
    }
}
