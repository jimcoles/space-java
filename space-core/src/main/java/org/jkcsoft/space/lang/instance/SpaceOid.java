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

/**
 * Encapsulates an object identifier used by the Space runtime to identify
 * and reference instance-level 'objects' such as Spaces, Tuples, and
 * Scalar Values.
 *
 * SpaceOid should never be made available to the user Space.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class SpaceOid implements Comparable<SpaceOid> {

    private long longOid = 0;

    SpaceOid(long longOid) {
        this.longOid = longOid;
    }

    public long getLongOid() {
        return longOid;
    }

    @Override
    public String toString() {
        return Long.toString(longOid);
    }

    @Override
    public int compareTo(SpaceOid o) {
        return Long.compare(this.longOid, o.getLongOid());
    }
}