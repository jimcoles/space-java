/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.intellij.plugin.psi;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import org.jkcsoft.space.intellij.plugin.SpaceFileType;

public class SpaceElementFactory {
  public static SimpleProperty createProperty(Project project, String name, String value) {
    final SpaceFile file = createFile(project, name + " = " + value);
    return (SimpleProperty) file.getFirstChild();
  }

  public static SimpleProperty createProperty(Project project, String name) {
    final SpaceFile file = createFile(project, name);
    return (SimpleProperty) file.getFirstChild();
  }

  public static PsiElement createCRLF(Project project) {
    final SpaceFile file = createFile(project, "\n");
    return file.getFirstChild();
  }

  public static SpaceFile createFile(Project project, String text) {
    String name = "dummy.simple";
    return (SpaceFile) PsiFileFactory.getInstance(project).
        createFileFromText(name, SpaceFileType.INSTANCE, text);
  }
}