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

import com.intellij.openapi.util.TextRange;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

public class SpaceReferenceContributor extends PsiReferenceContributor {
  @Override
  public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar) {
    registrar.registerReferenceProvider(PlatformPatterns.psiElement(PsiLiteralExpression.class),
                                        new PsiReferenceProvider() {
                                          @NotNull
                                          @Override
                                          public PsiReference[] getReferencesByElement(@NotNull PsiElement element,
                                                                                       @NotNull ProcessingContext
                                                                                           context) {
                                            PsiLiteralExpression literalExpression = (PsiLiteralExpression) element;
                                            String value = literalExpression.getValue() instanceof String ?
                                                (String) literalExpression.getValue() : null;
                                            if (value != null && value.startsWith("simple" + ":")) {
                                              return new PsiReference[]{
                                                  new SpaceReference(element, new TextRange(8, value.length() + 1))};
                                            }
                                            return PsiReference.EMPTY_ARRAY;
                                          }
                                        });
  }
}
