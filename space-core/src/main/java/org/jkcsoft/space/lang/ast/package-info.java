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
 * <p>This package represents, concretely, the Space Abstract Syntax Tree (AST).
 * The Space AST is the heart of the Java implementation of the language and as such
 * contains Java interfaces and classes corresponding to all language notions.</p>
 * <h2>Package Specification</h2>
 * NOTE: In the future, we may replace this Java implementation with a Space implementations
 * of the AST.
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
 *     <li>{@link org.jkcsoft.space.lang.ast.TypeDefnImpl} - A type corresponds to a type
 *     of thing, a noun, an entity. A type has state described via Variables and Associations.</li>
 *     <li>{@link org.jkcsoft.space.lang.ast.VariableDeclImpl} - A scalar-valued element
 *     with some semantic meaning associated with a Space</li>
 *     <li>EquationDefn</li>
 * </ul>
 * <p><em>Declarative</em> versus <em>Imperative</em> notions.</p>
 * <table border="true" padding="5">
 *     <tr><th>Declarative</th><th></th><th>Imperative</th></tr>
 *     <tr>
 *         <td>{@link org.jkcsoft.space.lang.ast.RuleImpl}</td>
 *         <td></td>
 *         <td>{@link org.jkcsoft.space.lang.ast.SpaceFunctionDefn}</td>
 *     </tr>
 * </table>
 *
 *
 * <h2>Related Documentation</h2>
 * @author Jim Coles
 */
package org.jkcsoft.space.lang.ast;