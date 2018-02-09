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

public class SpaceTokenType extends IElementType {
  public SpaceTokenType(@NotNull @NonNls String debugName) {
    super(debugName, SpaceLanguage.INSTANCE);
  }

  @Override
  public String toString() {
    return "SpaceTokenType." + super.toString();
  }
}