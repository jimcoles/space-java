/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * The central namespace registry: a point for registering named AST elements such as Schemas and
 * Types. Holds the entire collection and enforces name uniqueness.
 *
 * @author Jim Coles
 */
public class NSRegistry {

    private Set<Language> langRoots = new HashSet<>();
    {
        langRoots.add(Language.SPACE);
        langRoots.add(Language.JAVA);
    }

    private Named root;
    private Map<String, Named> allNamed = new TreeMap<>();

    public void addElement(Named newElement) {
        newElement.getNamedParent();
    }

}
