/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.runtime;

/**
 * The abstract base class defining basic behavior for obtaining a
 * Connection to a DataSystem regardless of the DataSystems sub-type.
 *
 * @author Jim Coles
 * @version 1.0
 */

public abstract class Connector {

    abstract Connection connect(ConnectionInfo connInfo)
            throws Exception;


}