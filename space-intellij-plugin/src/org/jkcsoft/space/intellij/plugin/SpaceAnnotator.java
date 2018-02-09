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

import com.intellij.lang.annotation.*;
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import org.jkcsoft.space.intellij.plugin.psi.SimpleProperty;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SpaceAnnotator implements Annotator {
  @Override
  public void annotate(@NotNull final PsiElement element, @NotNull AnnotationHolder holder) {
    if (element instanceof PsiLiteralExpression) {
      PsiLiteralExpression literalExpression = (PsiLiteralExpression) element;
      String value = literalExpression.getValue() instanceof String ? (String) literalExpression.getValue() : null;

      if (value != null && value.startsWith("simple" + ":")) {
        Project project = element.getProject();
        String key = value.substring(7);
        List<SimpleProperty> properties = SpaceUtil.findProperties(project, key);
        if (properties.size() == 1) {
          TextRange range = new TextRange(element.getTextRange().getStartOffset() + 7,
                                          element.getTextRange().getStartOffset() + 7);
          Annotation annotation = holder.createInfoAnnotation(range, null);
          annotation.setTextAttributes(DefaultLanguageHighlighterColors.LINE_COMMENT);
        } else if (properties.size() == 0) {
          TextRange range = new TextRange(element.getTextRange().getStartOffset() + 8,
                                          element.getTextRange().getEndOffset());
          holder.createErrorAnnotation(range, "Unresolved property").
              registerFix(new CreatePropertyQuickFix(key));
        }
      }
    }
  }
}
