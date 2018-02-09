/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.instance.SpaceOid;

/**
 * In pattern terms, the command processor.  Knows how to run a SpaceProgram
 * including executing Queries.
 *
 * @author J. Coles
 * @version 1.0
 */
public abstract class ExprProcessor {

    /**
     * The starting point for doing something. The exec method works iteratively
     * upon an ActionSequence.
     *
     * TODO: This is an excellent point to do some trace logging.
     */
//  abstract public ModelElement exec(ModelElement action) throws RuntimeException;


}
