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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Wraps Java Class to implement an AST Type Defn.
 *
 * @author Jim Coles
 */
public class SjiTypeDefn extends NamedElement implements ComplexType {

    // wrapped element
    private Class jClass;

    private List<Declartion> datumDecls = new LinkedList<>();
    private List<SjiFunctionDefnImpl> functionDefns = new LinkedList<>();

    SjiTypeDefn(Class jClass) {
        super(new NativeSourceInfo(jClass), jClass.getSimpleName());
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
    public boolean hasDatums() {
        return false;
    }

    @Override
    public List<Declartion> getDatumDeclList() {
        return datumDecls;
    }

    public void addAssociationDecl(SjiAssocDecl associationDecl) {
        addDatum(associationDecl);
    }

    public void addVariableDecl(SjiVarDecl variableDecl) {
        addDatum(variableDecl);
    }

    private void addDatum(SjiDeclaration declartion) {
        datumDecls.add(declartion);
    }

    public void addFunctionDefn(SjiFunctionDefnImpl sjiFunctionDefnImpl) {
        functionDefns.add(sjiFunctionDefnImpl);
    }

}
