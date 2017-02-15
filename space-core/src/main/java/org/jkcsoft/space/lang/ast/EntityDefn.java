/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */


package org.jkcsoft.space.lang.ast;

/**
 * A Entity space (definition) is very similar to a class (definition) in
 * conventional OOP.  Entities are base spaces upon which derived spaces --
 * Views, local spaces, and argument lists -- are defined.
 *
 * @author Jim Coles
 * @version 1.0
 * @deprecated Use SpaceDefn instead.
 */
public class EntityDefn extends SpaceDefn {

    EntityDefn(String name) {
        super(name);
    }

}
