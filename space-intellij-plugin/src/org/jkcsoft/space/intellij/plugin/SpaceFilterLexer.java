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

import com.intellij.lexer.Lexer;
import com.intellij.psi.impl.cache.impl.*;
import com.intellij.psi.search.UsageSearchContext;

public class SpaceFilterLexer extends BaseFilterLexer {
  public SpaceFilterLexer(final Lexer originalLexer, final OccurrenceConsumer table) {
    super(originalLexer, table);
  }

  @Override
  public void advance() {
    scanWordsInToken(UsageSearchContext.IN_COMMENTS, false, false);
    advanceTodoItemCountsInToken();
    myDelegate.advance();
  }
}
