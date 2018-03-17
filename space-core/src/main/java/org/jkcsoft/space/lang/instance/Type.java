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

import java.util.Map;

/**
 * The instance-level notion corresponding essentially to a row of data or to
 * an object in OOP.
 *
 * @author Jim Coles
 * @version 1.0
 */
public abstract class Type {


    public Type() {
    }



    /**
     * Get a Tuple value of Property named <code>name</code>
     */
    public abstract Object getValue(String codeName);

    public abstract Map getValueMap();


}
