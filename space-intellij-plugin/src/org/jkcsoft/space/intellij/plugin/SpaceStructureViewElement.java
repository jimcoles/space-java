/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
*/
package org.jkcsoft.space.intellij.plugin;

import com.intellij.ide.structureView.StructureViewTreeElement;
import com.intellij.ide.util.treeView.smartTree.*;
import com.intellij.navigation.*;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jkcsoft.space.intellij.plugin.psi.*;

import java.util.*;

public class SpaceStructureViewElement implements StructureViewTreeElement, SortableTreeElement {
  private PsiElement element;

  public SpaceStructureViewElement(PsiElement element) {
    this.element = element;
  }

  @Override
  public Object getValue() {
    return element;
  }

  @Override
  public void navigate(boolean requestFocus) {
    if (element instanceof NavigationItem) {
      ((NavigationItem) element).navigate(requestFocus);
    }
  }

  @Override
  public boolean canNavigate() {
    return element instanceof NavigationItem &&
           ((NavigationItem) element).canNavigate();
  }

  @Override
  public boolean canNavigateToSource() {
    return element instanceof NavigationItem &&
           ((NavigationItem) element).canNavigateToSource();
  }

  @Override
  public String getAlphaSortKey() {
    return element instanceof PsiNamedElement ? ((PsiNamedElement) element).getName() : null;
  }

  @Override
  public ItemPresentation getPresentation() {
    return element instanceof NavigationItem ?
        ((NavigationItem) element).getPresentation() : null;
  }

  @Override
  public TreeElement[] getChildren() {
    if (element instanceof SpaceFile) {
      SimpleProperty[] properties = PsiTreeUtil.getChildrenOfType(element, SimpleProperty.class);
      List<TreeElement> treeElements = new ArrayList<TreeElement>(properties.length);
      for (SimpleProperty property : properties) {
        treeElements.add(new SpaceStructureViewElement(property));
      }
      return treeElements.toArray(new TreeElement[treeElements.size()]);
    } else {
      return EMPTY_ARRAY;
    }
  }
}