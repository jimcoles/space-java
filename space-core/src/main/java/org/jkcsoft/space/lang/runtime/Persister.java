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

/**
 * The base class for persisters.  Defines basic scheme for loading and saving
 * object graphs based on object model information.  Uses classes in .data and
 * .data.model extensively.
 * <p>
 * In lieu of the usual IPersitable interface, this framework uses the
 * <code>.data.IObjectRecord</code> interface to indicate persistability.
 * IObjectRecord does not have save() and load() methods and therefore does not
 * allow the option of bean-managed persistence.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class Persister {


    public Persister() {
    }




}
