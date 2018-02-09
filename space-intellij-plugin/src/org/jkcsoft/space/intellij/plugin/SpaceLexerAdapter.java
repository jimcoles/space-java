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

import com.intellij.lexer.FlexAdapter;

import java.io.Reader;

public class SpaceLexerAdapter extends FlexAdapter {
  public SpaceLexerAdapter() {
    super(new SpaceLexer((Reader) null));
  }
}
