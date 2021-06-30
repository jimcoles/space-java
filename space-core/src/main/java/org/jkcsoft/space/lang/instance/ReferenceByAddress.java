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

/**
 * The actual physical (if virtual) memory pointer.
 * A hypothetical for Java unless we do our own byte array allocation
 * and state memory management.
 *
 * @author Jim Coles
 */
public class ReferenceByAddress extends AbstractReferenceValue {

    /** The start of the byte allocation for the object */
    private long byteOffset;

    @Override
    public Object getJavaValue() {
        return null;
    }
}
