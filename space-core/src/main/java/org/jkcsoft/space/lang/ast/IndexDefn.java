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

/**
 * NOTE: Etching this in, but not using yet. Initially, every Key definition will
 * get one Index that maps Key to object for the type. The index definition is
 * implied. Eventually we might want to let the user specify other indices such
 * on searchable variables or View filters.
 *
 * Structural elements:
 * <ul>
 *     <li>Key Definition - Projection variables (wrt one-and-only-one Type)
 * </ul>
 *
 * Synonyms: Map
 *
 * An Index is a mapping from one or more Variables to an object. Indexes enable
 * fast lookup of objects in a Space. Every {@link KeyDefn} has a corresponding Index.
 *
 * @author Jim Coles
 */
public interface IndexDefn extends Named {

}
