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
import java.util.Set;

/**
 * @author Jim Coles
 */
public abstract class AbstractCollectionTypeDefn extends AbstractTypeDefn implements CollectionType {

    private final TypeDefn containedElementType;

    protected AbstractCollectionTypeDefn(SourceInfo sourceInfo,
                                         String name,
                                         TypeDefn containedElementType)
    {
        super(sourceInfo, name);
        this.containedElementType = containedElementType;
    }

    public TypeDefn getContainedElementType() {
        return containedElementType;
    }

    @Override
    public int getScalarDofs() {
        return containedElementType.getScalarDofs();
    }

    @Override
    public KeyDefn getPrimaryKeyDefn() {
        return containedElementType.getPrimaryKeyDefn();
    }

    @Override
    public Set<KeyDefn> getAllKeyDefns() {
        return containedElementType.getAllKeyDefns();
    }

    @Override
    public boolean hasName() {
        return containedElementType.hasName();
    }

    @Override
    public boolean isPrimitiveType() {
        return false;
    }

    @Override
    public VariableDecl addVariableDecl(VariableDecl variableDecl) {
        return containedElementType.addVariableDecl(variableDecl);
    }

    @Override
    public AssociationDefn addAssociationDecl(AssociationDefn associationDecl) {
        return containedElementType.addAssociationDecl(associationDecl);
    }

    @Override
    public List<VariableDecl> getVariables() {
        return containedElementType.getVariables();
    }

    @Override
    public List<Declaration> getDatumDecls() {
        return containedElementType.getDatumDecls();
    }

    @Override
    public StatementBlock getInitBlock() {
        return containedElementType.getInitBlock();
    }

    @Override
    public FunctionDefn addFunctionDefn(FunctionDefn functionDefn) {
        return containedElementType.addFunctionDefn(functionDefn);
    }

    @Override
    public boolean hasDatums() {
        return containedElementType.hasDatums();
    }

    /**
     * Always false because even a collection of primitives or Simple Types is not itself
     * a simple type.
     */
    @Override
    public boolean isSimpleType() {
        return false;
    }

    @Override
    public boolean isComplexType() {
        return false;
    }

    /** True if this set hold references to objects; false if it hold values of primitives */
    public boolean isReferenceType() {
        return containedElementType instanceof TypeDefnImpl;
    }

    @Override
    public List<String> getFullNamePath() {
        return null;
    }

    @Override
    public boolean isAssignableTo(TypeDefn receivingType) {
        return false;
    }
}
