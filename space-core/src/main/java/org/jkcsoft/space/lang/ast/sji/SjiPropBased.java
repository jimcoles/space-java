/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast.sji;

import org.jkcsoft.space.lang.ast.DatumDecl;

import java.beans.PropertyDescriptor;

/**
 * @author Jim Coles
 */
public interface SjiPropBased extends DatumDecl {

    PropertyDescriptor getjPropDesc();

}
