/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.AssociationDefn;

/**
 * {@link ReferenceValue}'s are instance-level counterparts to {@link AssociationDefn}'s.
 * A {@link ReferenceValue} is analogous to a Linux 'link', both hard links and symbolic links.
 *
 * @author Jim Coles
 */
public interface ReferenceValue<J> extends Value<J> {

}
