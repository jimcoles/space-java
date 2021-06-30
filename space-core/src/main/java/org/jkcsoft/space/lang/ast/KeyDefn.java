/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import java.util.List;

/**
 * Structural elements:
 * <ul>
 *     <li>Projection variables (wrt one-and-only-one basis Type)
 * </ul>
 *
 * <p>A KeyDefn is simply a sequence of variables associated with a Type.
 * A general means of defining primary keys, alternate keys, and external keys.
 * Keys may be a simple single variable, opaque byte or as complex as tuple of
 * scalar variables.
 *
 * <p>Keys enable the following high-level language features:
 *
 * <ul>
 *   <li>Language-managed indexes and trees.
 *   <li>CDI-like binding and lookup of objects by key, and injection of objects.
 *   <li>Mapping to key-oriented database models.
 *   <li>Simple binding of configuration data to related meta objects (user Types,
 *       Associations).
 * </ul>
 *
 * <p>4GL: Defining keys at the language level moves Space in the 4GL direction.
 *
 * @author Jim Coles
 */
public interface KeyDefn extends ViewDefn {

}
