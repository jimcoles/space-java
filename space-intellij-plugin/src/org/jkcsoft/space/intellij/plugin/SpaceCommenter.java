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

import com.intellij.lang.Commenter;
import org.jetbrains.annotations.Nullable;

public class SpaceCommenter implements Commenter {
  @Nullable
  @Override
  public String getLineCommentPrefix() {
    return "#";
  }

  @Nullable
  @Override
  public String getBlockCommentPrefix() {
    return "";
  }

  @Nullable
  @Override
  public String getBlockCommentSuffix() {
    return null;
  }

  @Nullable
  @Override
  public String getCommentedBlockCommentPrefix() {
    return null;
  }

  @Nullable
  @Override
  public String getCommentedBlockCommentSuffix() {
    return null;
  }
}