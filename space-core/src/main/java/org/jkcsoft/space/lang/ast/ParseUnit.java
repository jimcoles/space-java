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

import java.util.List;

@GroupingNode
public class ParseUnit extends ModelElement {

//    private File srcFile;

    private PackageDecl packageDecl;
    private List<SpacePathExpr> imports;

    ParseUnit(SourceInfo sourceInfo) {
        super(sourceInfo);
//        this.srcFile = srcFile;
    }

    public PackageDecl getPackageDecl() {
        return packageDecl;
    }

//    public File getSrcFile() {
//        return srcFile;
//    }
}
