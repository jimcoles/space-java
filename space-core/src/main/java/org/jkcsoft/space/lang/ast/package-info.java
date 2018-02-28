/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

/**
 * <p>This package defines the Abstract Syntax Tree (AST) for the Space language and
 * is the heart of the Java implementation of Space.  The package contains classes
 * representing all language notions for Space including
 * non-terminals and terminals.  These are the types of things one sees in
 * a given Space source code file.</p>
 * <h2>Package Specification</h2>
 * Eventually we will have a version of this package that is itself generated
 * from a Space model.
 * <p>
 * Many element classes are named per the following: <br>
 * <p>
 * xyzDefn - the named defining expression<br>
 * xyzExpr - the defining expression<br>
 * xyz - the runtime instance that adheres to its corresponding definition, xyzDefn<br>
 * <p>This applies to Transform, Equation, Function, and Type.
 * <h2>Primary Notions</h2>
 * <p>The following are concrete notions defined by a Space programmer.</p>
 * <ul>
 *     <li>SpaceDefn - A Space is a thing, a noun, an entity. A Space has state
 *     described via Variables and Associations.</li>
 *     <li>{@link org.jkcsoft.space.lang.ast.VariableDefn} - A scalar-valued element
 *     with some semantic meaning associated with a Space</li>
 *     <li>EquationDefn</li>
 * </ul>
 * <p><em>Declarative</em> versus <em>Imperative</em> notions.</p>
 * <table border="true" padding="5">
 *     <tr><th>Declarative</th><th></th><th>Imperative</th></tr>
 *     <tr>
 *         <td>{@link org.jkcsoft.space.lang.ast.EquationDefn}</td>
 *         <td></td>
 *         <td>{@link org.jkcsoft.space.lang.ast.FunctionDefn}</td>
 *     </tr>
 * </table>
 *
 *
 * <h2>Related Documentation</h2>
 * @author Jim Coles
 */
package org.jkcsoft.space.lang.ast;