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

import com.intellij.psi.tree.IElementType;
import org.jkcsoft.space.intellij.plugin.SpaceLanguage;
import org.jetbrains.annotations.*;

public class SpaceElementType extends IElementType {
  public SpaceElementType(@NotNull @NonNls String debugName) {
    super(debugName, SpaceLanguage.INSTANCE);
  }
}