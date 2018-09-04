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

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@GroupingNode
public class ParseUnit extends ModelElement {

    private PackageDecl packageDecl;
    private List<ImportExpr> importExprExprs;
    // redundant list of resolved import types after wildcard expansion
    private Set<DatumType> allImportedTypes;

    private List<ComplexType> typeDefns = new LinkedList<>();

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

    public void addImportExpr(ImportExpr importExprExpr) {
        if (importExprExprs == null)
            importExprExprs = new LinkedList<>();

        importExprExprs.add(importExprExpr);
        //
        addChild(importExprExpr);
    }

    public boolean hasImports() {
        return importExprExprs != null && !importExprExprs.isEmpty();
    }

    public List<ImportExpr> getImportExprExprs() {
        return importExprExprs;
    }

    public void addToImportTable(DatumType datumType) {
        allImportedTypes.add(datumType);
    }

    public Set<DatumType> getAllImportedTypes() {
        return allImportedTypes;
    }

    public ComplexType addTypeDefn(ComplexType spaceTypeDefn) {
        typeDefns.add(spaceTypeDefn);
        //
        addChild((NamedElement) spaceTypeDefn);
        return spaceTypeDefn;
    }

    public StreamTypeDefn addStreamTypeDefn(StreamTypeDefn streamTypeDefn) {
        addChild(streamTypeDefn);
        return streamTypeDefn;
    }

    public List<ComplexType> getTypeDefns() {
        return typeDefns;
    }

}
