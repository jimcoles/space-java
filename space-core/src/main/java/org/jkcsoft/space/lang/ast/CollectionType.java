/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * Collection types are collections of one or more other types. Similar to Java
 * parameterized collections but all parameters are carried along into
 * runtime for type validation purposes. Also, the Space language
 * knows about these. In contrast, Java knows about arrays (and their types)
 * but does not know about java.util.Collection notions.
 *
 * @author Jim Coles
 */
public interface CollectionType extends DatumType {

}
