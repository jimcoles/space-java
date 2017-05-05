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

import com.intellij.navigation.*;
import com.intellij.openapi.project.Project;
import org.jkcsoft.space.intellij.plugin.psi.SimpleProperty;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SpaceChooseByNameContributor implements ChooseByNameContributor {
  @NotNull
  @Override
  public String[] getNames(Project project, boolean includeNonProjectItems) {
    List<SimpleProperty> properties = SpaceUtil.findProperties(project);
    List<String> names = new ArrayList<String>(properties.size());
    for (SimpleProperty property : properties) {
      if (property.getKey() != null && property.getKey().length() > 0) {
        names.add(property.getKey());
      }
    }
    return names.toArray(new String[names.size()]);
  }

  @NotNull
  @Override
  public NavigationItem[] getItemsByName(String name, String pattern, Project project, boolean includeNonProjectItems) {
    // todo include non project items
    List<SimpleProperty> properties = SpaceUtil.findProperties(project, name);
    return properties.toArray(new NavigationItem[properties.size()]);
  }
}