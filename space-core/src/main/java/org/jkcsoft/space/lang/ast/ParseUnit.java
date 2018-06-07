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

import java.util.ArrayList;
import java.util.List;

@GroupingNode
public class ParseUnit extends ModelElement {

    private PackageDecl packageDecl;
    private List<TypeRef> imports;

    ParseUnit(SourceInfo sourceInfo) {
        super(sourceInfo);
//        this.srcFile = srcFile;
    }

    public PackageDecl getPackageDecl() {
        return packageDecl;
    }

    public void setPackageDecl(PackageDecl packageDecl) {
        this.packageDecl = packageDecl;
    }

    public void addImports(List<TypeRef> imports) {
        if (imports == null)
            this.imports = new ArrayList<>(imports);
        else
            this.imports.addAll(imports);
    }

    public List<TypeRef> getImports() {
        return imports;
    }

    public boolean hasImports() {
        return imports != null && !imports.isEmpty();
    }
}
