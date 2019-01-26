/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast.sji;

import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.LinkedList;
import java.util.List;

/**
 * Wraps Java Class to implement an AST Type Defn.
 *
 * @author Jim Coles
 */
public class SjiTypeDefn extends NamedElement implements ComplexType {

    // wrapped element
    private Class jClass;

    private List<Declaration> datumDecls = new LinkedList<>();
    private List<SjiFunctionDefnImpl> functionDefns = new LinkedList<>();

    SjiTypeDefn(Class jClass) {
        super(new NativeSourceInfo(jClass), jClass == null ? "(method args)" : jClass.getSimpleName());
        this.jClass = jClass;
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.TYPE;
    }

    @Override
    public int getScalarDofs() {
        return 0;
    }

    @Override
    public SequenceTypeDefn getSequenceOfType() {
        return null;
    }

    @Override
    public boolean isPrimitiveType() {
        return false;
    }

    @Override
    public boolean isAssignableTo(DatumType argsType) {
        return false;
    }

    @Override
    public boolean hasDatums() {
        return false;
    }

    @Override
    public List<Declaration> getDatumDeclList() {
        return datumDecls;
    }

    public void addAssociationDecl(AssociationDecl associationDecl) {
        addDatum(associationDecl);
        //
        addChild((ModelElement) associationDecl);
    }

    public void addVariableDecl(SjiVarDecl variableDecl) {
        addDatum(variableDecl);
        //
        addChild(variableDecl);
    }

    private void addDatum(Declaration declaration) {
        datumDecls.add(declaration);
    }

    public void addFunctionDefn(SjiFunctionDefnImpl sjiFunctionDefnImpl) {
        functionDefns.add(sjiFunctionDefnImpl);
        //
        addChild(sjiFunctionDefnImpl);
    }

}
