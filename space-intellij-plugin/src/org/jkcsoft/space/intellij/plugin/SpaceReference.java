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

import com.intellij.codeInsight.lookup.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import org.jkcsoft.space.intellij.plugin.psi.SimpleProperty;
import org.jetbrains.annotations.*;

import java.util.*;

public class SpaceReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference {
  private String key;

  public SpaceReference(@NotNull PsiElement element, TextRange textRange) {
    super(element, textRange);
    key = element.getText().substring(textRange.getStartOffset(), textRange.getEndOffset());
  }

  @NotNull
  @Override
  public ResolveResult[] multiResolve(boolean incompleteCode) {
    Project project = myElement.getProject();
    final List<SimpleProperty> properties = SpaceUtil.findProperties(project, key);
    List<ResolveResult> results = new ArrayList<ResolveResult>();
    for (SimpleProperty property : properties) {
      results.add(new PsiElementResolveResult(property));
    }
    return results.toArray(new ResolveResult[results.size()]);
  }

  @Nullable
  @Override
  public PsiElement resolve() {
    ResolveResult[] resolveResults = multiResolve(false);
    return resolveResults.length == 1 ? resolveResults[0].getElement() : null;
  }

  @NotNull
  @Override
  public Object[] getVariants() {
    Project project = myElement.getProject();
    List<SimpleProperty> properties = SpaceUtil.findProperties(project);
    List<LookupElement> variants = new ArrayList<LookupElement>();
    for (final SimpleProperty property : properties) {
      if (property.getKey() != null && property.getKey().length() > 0) {
        variants.add(LookupElementBuilder.create(property).
            withIcon(SpaceIcons.FILE).
                                             withTypeText(property.getContainingFile().getName())
        );
      }
    }
    return variants.toArray();
  }
}