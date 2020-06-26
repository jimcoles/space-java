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
public class ParseUnit extends AbstractModelElement {

    private PackageDecl packageDecl;
    private List<ImportExpr> importExprs;
    // redundant list of resolved import types after wildcard expansion
    private Set<TypeDefn> allImportedTypes = null;

    private List<TypeDefn> typeDefns = new LinkedList<>();
    private IntrinsicContainer importedTypeContainer;

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

    public void addToAllImportedTypes(TypeDefn typeDefn) {
        if (allImportedTypes == null)
            allImportedTypes = new HashSet<>();

        allImportedTypes.add(typeDefn);
    }

    public Set<TypeDefn> getAllImportedTypes() {
        return allImportedTypes != null ? allImportedTypes : Collections.EMPTY_SET;
    }

    public TypeDefn addTypeDefn(TypeDefn spaceTypeDefn) {
        typeDefns.add(spaceTypeDefn);
        //
        addChild((NamedElement) spaceTypeDefn);
        return spaceTypeDefn;
    }

    public StreamTypeDefn addStreamTypeDefn(StreamTypeDefn streamTypeDefn) {
        addChild(streamTypeDefn);
        return streamTypeDefn;
    }

    public List<TypeDefn> getTypeDefns() {
        return typeDefns;
    }

    public ModelElement getImportedTypeContainer() {
        if (importedTypeContainer == null) {
            Set<TypeDefn> allImportedTypes = this.getAllImportedTypes();
            importedTypeContainer = AstFactory.getInstance().newIntrinsicContainer();
            allImportedTypes.forEach(dt -> importedTypeContainer.addChild(dt));
        }
        return importedTypeContainer;
    }
}
