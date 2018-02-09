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

import com.intellij.extapi.psi.PsiFileBase;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.psi.FileViewProvider;
import org.jkcsoft.space.intellij.plugin.*;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class SpaceFile extends PsiFileBase {
  public SpaceFile(@NotNull FileViewProvider viewProvider) {
    super(viewProvider, SpaceLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public FileType getFileType() {
    return SpaceFileType.INSTANCE;
  }

  @Override
  public String toString() {
    return "Simple File";
  }

  @Override
  public Icon getIcon(int flags) {
    return super.getIcon(flags);
  }
}