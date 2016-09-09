/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.langmaps.umlish;

/**
 * Encapsulates an object identifier within the Systematic data management
 * system.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class Oid extends Identifier {
    private long _oid;

    public Oid() {
    }

    public long getOid() {
        return _oid;
    }

}