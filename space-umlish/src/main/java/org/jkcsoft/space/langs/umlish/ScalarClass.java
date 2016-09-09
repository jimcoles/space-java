/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.langmaps.umlish;

/**
 * A scalar Class is one that maps to one and only one PrimitiveDataType.
 * For example, a Date maps to a long in most models.  In the SSC metamodel,
 * attributes can be of type PrimitiveDataType or ScalarClass, but can not be
 * of a non-scalar Class.  Modelers must create an Association to achive the
 * effect of giving a Class an 'attribute' of a type that is a non-scalar Class.
 * <p>
 * In mapping to a database, a ScalarClass can get its own Table whereas a
 * PrimitiveDataType can not.
 * <p>
 * NOTE: This might not need its own Java class.
 *
 * @author J. Coles
 * @version 1.0
 */
public class ScalarClass {
    //----------------------------------------------------------------------------
     // Instance-level


    public ScalarClass() {
    }




}
