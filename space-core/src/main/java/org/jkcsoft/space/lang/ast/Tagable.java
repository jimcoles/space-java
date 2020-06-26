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

import org.jkcsoft.space.lang.instance.Tuple;
import org.jkcsoft.space.lang.instance.TupleSetImpl;

import java.util.List;

/**
 * <p>Tags are similar to Java Annotations. They provide a consistent mechanism
 * to attribute meta object: types, associations, functions, etc.. with data that
 * can drive a wide range of compile-time or runtime processing including,
 * <ul>
 *   <li>Equivalent counterparts to language-level modifiers such as access control,
 *     optionality.
 *   <li>Persistence
 *   <li>In-memory object management like CDI and indexing
 *   <li>Logging
 *   <li>Code-gen mappings, e.g., generate C++, Javascript
 *   <li>Mappings to intermediate model layers, internal and external: Could be used to
 *      emulate usage of UML (tool) such as Rational Rose.
 * </ul>
 * <p>DIFFERATOR: Unlike Java, Space does not limit the type of object associated with a tag.
 *
 * <p>DIFFERATOR: Tags may be embedded directly in source code or loaded from related stores
 * including source files, resource files, LDAP directories, etc..
 *
 * <p>The primary {@link Tagable} is {@link TypeDefnImpl}.
 * We might also make user objects {@link Tagable}. ISSUE: Is that good idea? Could lead to
 * abuse.
 *
 * @author Jim Coles
 */
public interface Tagable {

    /** Return tags as a {@link TupleSetImpl}. */
    List<Tuple> getAllTagsList();

}
