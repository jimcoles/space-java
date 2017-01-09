/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import org.jkcsoft.space.lang.instance.Space;

/**
 * Encapsulates an entire executable system as defined by Space definition elements
 * (ModelElements) and associated instances.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class SpaceProgram extends ModelElement {
    Space _relationDefns;
    Space _assocDefns;
    Space _actionSequenceDefns;

    // TODO: indexes for fast lookup


    public SpaceProgram() {
    }


}
