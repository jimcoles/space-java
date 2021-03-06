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

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import org.jkcsoft.space.intellij.plugin.psi.*;

import java.util.*;

public class SpaceUtil {
  public static List<SimpleProperty> findProperties(Project project, String key) {
    List<SimpleProperty> result = null;
    Collection<VirtualFile> virtualFiles =
        FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, SpaceFileType.INSTANCE,
                                                        GlobalSearchScope.allScope(project));
    for (VirtualFile virtualFile : virtualFiles) {
      SpaceFile spaceFile = (SpaceFile) PsiManager.getInstance(project).findFile(virtualFile);
      if (spaceFile != null) {
        SimpleProperty[] properties = PsiTreeUtil.getChildrenOfType(spaceFile, SimpleProperty.class);
        if (properties != null) {
          for (SimpleProperty property : properties) {
            if (key.equals(property.getKey())) {
              if (result == null) {
                result = new ArrayList<SimpleProperty>();
              }
              result.add(property);
            }
          }
        }
      }
    }
    return result != null ? result : Collections.<SimpleProperty>emptyList();
  }

  public static List<SimpleProperty> findProperties(Project project) {
    List<SimpleProperty> result = new ArrayList<SimpleProperty>();
    Collection<VirtualFile> virtualFiles =
        FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, SpaceFileType.INSTANCE,
                                                        GlobalSearchScope.allScope(project));
    for (VirtualFile virtualFile : virtualFiles) {
      SpaceFile spaceFile = (SpaceFile) PsiManager.getInstance(project).findFile(virtualFile);
      if (spaceFile != null) {
        SimpleProperty[] properties = PsiTreeUtil.getChildrenOfType(spaceFile, SimpleProperty.class);
        if (properties != null) {
          Collections.addAll(result, properties);
        }
      }
    }
    return result;
  }
}