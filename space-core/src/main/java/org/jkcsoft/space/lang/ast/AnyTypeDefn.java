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
 * A 'choice' wrapper of our different types: primitive, domain, space, stream.
 *
 * @author Jim Coles
 */
public class AnyTypeDefn {

    private NumPrimitiveTypeDefn primitiveTypeDefn;
    private SimpleType simpleType;
    private ComplexType complexType;
    private StreamTypeDefn streamTypeDefn;

}
