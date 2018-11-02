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
    private Namespace[] nsLookupChain;
    private Directory[] rootDirLookupChain;

    Namespace(SourceInfo sourceInfo, String name, Namespace ... nsLookupChain) {
        super(sourceInfo, name);
        this.rootDir = AstFactory.getInstance().newAstDir(new IntrinsicSourceInfo(), "(root)");
        this.nsLookupChain = new Namespace[nsLookupChain.length + 1];
        this.nsLookupChain[0] = this;
        for (int idxExtraLookup = 0; idxExtraLookup < nsLookupChain.length; idxExtraLookup++) {
            this.nsLookupChain[idxExtraLookup + 1] = nsLookupChain[idxExtraLookup];
        }
        this.rootDirLookupChain = new Directory[this.nsLookupChain.length];
        for (int idxChain = 0; idxChain < this.nsLookupChain.length; idxChain++) {
            this.rootDirLookupChain[idxChain] = this.nsLookupChain[idxChain].getRootDir();
        }
        //
        this.addChild(rootDir);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.PACKAGE;
    }

    public Directory getRootDir() {
        return rootDir;
    }

    public Namespace[] getNsLookupChain() {
        return nsLookupChain;
    }

    public Directory[] getRootDirLookupChain() {
        return rootDirLookupChain;
    }

    @Override
    public String getDisplayName() {
        return super.getDisplayName() + ":";
    }
}
