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

import com.intellij.openapi.fileTypes.LanguageFileType;
import org.jetbrains.annotations.*;

import javax.swing.*;

public class SpaceFileType extends LanguageFileType {
  public static final SpaceFileType INSTANCE = new SpaceFileType();

  private SpaceFileType() {
    super(SpaceLanguage.INSTANCE);
  }

  @NotNull
  @Override
  public String getName() {
    return "Space source file";
  }

  @NotNull
  @Override
  public String getDescription() {
    return "Space language file";
  }

  @NotNull
  @Override
  public String getDefaultExtension() {
    return "space";
  }

  @Nullable
  @Override
  public Icon getIcon() {
    return SpaceIcons.FILE;
  }
}