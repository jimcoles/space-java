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

import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * A named root of a Directory tree. A given runtime context will contain multiple Namespaces.
 * @author Jim Coles
 */
public class Namespace extends NamedElement {

    private Directory rootDir;

    Namespace(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
        rootDir = AstFactory.getInstance().newAstDir(new IntrinsicSourceInfo(), "/");
        this.addChild(rootDir);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.PACKAGE;
    }

    public Directory getRootDir() {
        return rootDir;
    }

    @Override
    public String getDisplayName() {
        return super.getDisplayName() + ":";
    }
}
