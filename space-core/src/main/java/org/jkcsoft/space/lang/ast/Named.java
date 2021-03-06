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

import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.List;

/**
 * @author Jim Coles
 */
public interface Named extends Identified {

    boolean hasName();

    NamePart getNamePart();

    String getName();

    List<String> getFullNamePath();

    Named getNamedParent();

    String getFQName();

    MetaType getMetaType();
}
