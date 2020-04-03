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
 * @author Jim Coles
 */
public class ReferenceByKey extends AbstractReferenceValue<Tuple> {

    private Tuple keyValue;

    public ReferenceByKey(Tuple keyValue) {
        this.keyValue = keyValue;
    }

    @Override
    public Tuple getJavaValue() {
        return keyValue;
    }

    public Tuple getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(Tuple keyValue) {
        this.keyValue = keyValue;
    }

}
