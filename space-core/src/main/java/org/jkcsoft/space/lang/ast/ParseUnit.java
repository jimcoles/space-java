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

import java.util.*;

@GroupingNode
public class ParseUnit extends ModelElement {

    private PackageDecl packageDecl;
    private List<ImportExpr> importExprs;
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
        if (importExprs == null)
            importExprs = new LinkedList<>();

        importExprs.add(importExprExpr);
        //
        addChild(importExprExpr);
    }

    public boolean hasImports() {
        return importExprs != null && !importExprs.isEmpty();
    }

    public List<ImportExpr> getImportExprs() {
        if (importExprs == null)
            importExprs = Collections.emptyList();
        return importExprs;
    }

    public void addToAllImportedTypes(DatumType datumType) {
        if (allImportedTypes == null)
            allImportedTypes = new HashSet<>();

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
