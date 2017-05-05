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

import com.intellij.psi.codeStyle.*;

public class SpaceCodeStyleSettings extends CustomCodeStyleSettings {
  public SpaceCodeStyleSettings(CodeStyleSettings settings) {
    super("SimpleCodeStyleSettings", settings);
  }
}