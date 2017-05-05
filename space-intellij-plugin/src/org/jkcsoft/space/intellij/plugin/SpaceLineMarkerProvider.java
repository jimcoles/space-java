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

import com.intellij.codeInsight.daemon.*;
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jkcsoft.space.intellij.plugin.psi.SimpleProperty;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SpaceLineMarkerProvider extends RelatedItemLineMarkerProvider {
  @Override
  protected void collectNavigationMarkers(@NotNull PsiElement element,
                                          Collection<? super RelatedItemLineMarkerInfo> result) {
    if (element instanceof PsiLiteralExpression) {
      PsiLiteralExpression literalExpression = (PsiLiteralExpression) element;
      String value = literalExpression.getValue() instanceof String ? (String) literalExpression.getValue() : null;
      if (value != null && value.startsWith("simple" + ":")) {
        Project project = element.getProject();
        final List<SimpleProperty> properties = SpaceUtil.findProperties(project, value.substring(7));
        if (properties.size() > 0) {
          NavigationGutterIconBuilder<PsiElement> builder =
              NavigationGutterIconBuilder.create(SpaceIcons.FILE).
                  setTargets(properties).
                                             setTooltipText("Navigate to a simple property");
          result.add(builder.createLineMarkerInfo(element));
        }
      }
    }
  }
}
