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

import org.jkcsoft.java.util.Strings;

import java.text.MessageFormat;

/**
 * Holds a reference to a Space "object", which might be a Tuple or any
 * non-scalar such as a Stream or Sequence.
 *
 * @author Jim Coles
 */
public class ReferenceByOid extends AbstractReferenceValue<SpaceOid> {

    /** Oid of the referenced 'to' object */
    private SpaceOid toOid;

    ReferenceByOid(SpaceOid toOid) {
        this.toOid = toOid;
    }

    public void setToOid(SpaceOid toOid) {
        this.toOid = toOid;
    }

    public SpaceOid getToOid() {
        return toOid;
    }

    @Override
    public SpaceOid getJavaValue() {
        return toOid;
    }

    @Override
    public String toString() {
        return MessageFormat.format("Internal reference by oid = {0} -> ", toOid);
    }

}
