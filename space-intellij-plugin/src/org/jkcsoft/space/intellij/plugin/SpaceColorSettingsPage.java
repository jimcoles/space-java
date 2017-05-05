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

import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.options.colors.*;
import org.jetbrains.annotations.*;

import javax.swing.*;
import java.util.Map;

public class SpaceColorSettingsPage implements ColorSettingsPage {
  private static final AttributesDescriptor[] DESCRIPTORS = new AttributesDescriptor[]{
      new AttributesDescriptor("Key", SpaceSyntaxHighlighter.KEY),
      new AttributesDescriptor("Separator", SpaceSyntaxHighlighter.SEPARATOR),
      new AttributesDescriptor("Value", SpaceSyntaxHighlighter.VALUE),
  };

  @Nullable
  @Override
  public Icon getIcon() {
    return SpaceIcons.FILE;
  }

  @NotNull
  @Override
  public SyntaxHighlighter getHighlighter() {
    return new SpaceSyntaxHighlighter();
  }

  @NotNull
  @Override
  public String getDemoText() {
    return "# You are reading the \".properties\" entry.\n" +
           "! The exclamation mark can also mark text as comments.\n" +
           "website = http://en.wikipedia.org/\n" +
           "language = English\n" +
           "# The backslash below tells the application to continue reading\n" +
           "# the value onto the next line.\n" +
           "message = Welcome to \\\n" +
           "          Wikipedia!\n" +
           "# Add spaces to the key\n" +
           "key\\ with\\ spaces = This is the value that could be looked up with the key \"key with spaces\".\n" +
           "# Unicode\n" +
           "tab : \\u0009";
  }

  @Nullable
  @Override
  public Map<String, TextAttributesKey> getAdditionalHighlightingTagToDescriptorMap() {
    return null;
  }

  @NotNull
  @Override
  public AttributesDescriptor[] getAttributeDescriptors() {
    return DESCRIPTORS;
  }

  @NotNull
  @Override
  public ColorDescriptor[] getColorDescriptors() {
    return ColorDescriptor.EMPTY_ARRAY;
  }

  @NotNull
  @Override
  public String getDisplayName() {
    return "Simple";
  }
}