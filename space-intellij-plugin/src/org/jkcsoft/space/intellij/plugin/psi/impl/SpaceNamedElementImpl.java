/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
*/
package org.jkcsoft.space.intellij.plugin.psi.impl;

import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.lang.ASTNode;
import org.jkcsoft.space.intellij.plugin.psi.SpaceNamedElement;
import org.jetbrains.annotations.NotNull;

public abstract class SpaceNamedElementImpl extends ASTWrapperPsiElement implements SpaceNamedElement {
  public SpaceNamedElementImpl(@NotNull ASTNode node) {
    super(node);
  }
}