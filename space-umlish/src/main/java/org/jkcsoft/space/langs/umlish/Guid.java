/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.langmaps.umlish;

/**
 * A globally unique object identifier.
 *
 * @author Jim Coles
 * @version 1.0
 */

public class Guid extends Identifier {
    private byte _bytes[] = new byte[8];

    public Guid() {
    }

    public byte[] getBytes() {
        return _bytes;
    }

}