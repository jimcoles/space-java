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

import com.intellij.formatting.*;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import org.jkcsoft.space.intellij.plugin.psi.SimpleTypes;
import org.jetbrains.annotations.*;

public class SpaceFormattingModelBuilder implements FormattingModelBuilder {
  @NotNull
  @Override
  public FormattingModel createModel(PsiElement element, CodeStyleSettings settings) {
    return FormattingModelProvider.createFormattingModelForPsiFile(element.getContainingFile(),
                                                                   new SpaceBlock(element.getNode(),
                                                                                  Wrap.createWrap(WrapType.NONE,
                                                                                                   false),
                                                                                  Alignment.createAlignment(),
                                                                                  createSpaceBuilder(settings)),
                                                                   settings);
  }

  private static SpacingBuilder createSpaceBuilder(CodeStyleSettings settings) {
    return new SpacingBuilder(settings, SpaceLanguage.INSTANCE).
                                                                    around(SimpleTypes.SEPARATOR)
                                                                .spaceIf(settings.SPACE_AROUND_ASSIGNMENT_OPERATORS)
                                                                .
                                                                    before(SimpleTypes.PROPERTY)
                                                                .none();
  }

  @Nullable
  @Override
  public TextRange getRangeAffectingIndent(PsiFile file, int offset, ASTNode elementAtOffset) {
    return null;
  }
}