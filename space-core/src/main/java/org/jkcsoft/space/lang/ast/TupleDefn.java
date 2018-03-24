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

import org.jkcsoft.space.lang.instance.Tuple;

import java.util.List;

/**
 * @author Jim Coles
 */
public interface TupleDefn {

    boolean hasVariables();

    List<VariableDefn> getVariableDefnList();

    boolean hasAssociations();

    List<AssociationDefn> getAssociationDefnList();

    List<NamedElement> getAllMembers();
}
