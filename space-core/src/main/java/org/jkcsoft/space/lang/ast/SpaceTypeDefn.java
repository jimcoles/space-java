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

import java.util.List;

/**
 * The central declarative, data-centric, meta notion of Space.
 * <p>
 * OOP Analog: Class (definition)<br>
 * RDB Analog: Table definition (create table ... statement)
 *
 * @author Jim Coles
 * @version 1.0
 */
public class SpaceTypeDefn extends NamedElement implements DatumType, TupleDefn {

    private boolean isEntity;
    private SpaceTypeDefnBody body;
    private SetTypeDefn setTypeDefn;
    private SequenceTypeDefn sequenceTypeDefn;

    SpaceTypeDefn(SourceInfo sourceInfo, TextNode nameNode) {
        super(sourceInfo, nameNode.getText());
        this.setTypeDefn = new SetTypeDefn(sourceInfo, this);
        sequenceTypeDefn = new SequenceTypeDefn(getSourceInfo(), this);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.TYPE;
    }

    @Override
    public int getScalarDofs() {
        return body.getScalarDofs();
    }

    @Override
    public SequenceTypeDefn getSequenceOfType() {
        return sequenceTypeDefn;
    }

    public boolean isComputed() {
        return body.isComputed();
    }

    public boolean isEnumerated() {
        return body.isEnumerated();
    }

    public SetTypeDefn getSetTypeDefn() {
        return setTypeDefn;
    }

    /**
     * Might not be needed; essentially, asking 'is this a basis type' or a 'derived' type.
     */
    public boolean isEntity() {
        return isEntity;
    }

    public List<TransformDefn> getTransformDefns() {
        return body.getTransformDefns();
    }

    public List<AbstractFunctionDefn> getFunctionDefns() {
        return body.getFunctionDefns();
    }

    public VariableDecl addVariable(VariableDecl variableDecl) {
        return body.addVariableDecl(variableDecl);
    }

    public AssociationDecl addAssocDefn(AssociationDecl associationDecl) {
        return body.addAssocDecl(associationDecl);
    }

    public AbstractFunctionDefn addFunctionDefn(AbstractFunctionDefn functionDefn) {
        return body.addFunctionDefn(functionDefn);
    }

    public SpaceTypeDefnBody setBody(SpaceTypeDefnBody body) {
        this.body = body;
        //
        addChild(body);
        //
        return body;
    }

    public SpaceTypeDefnBody getBody() {
        return body;
    }

    @Override
    public boolean hasDatums() {
        return body.hasDatums();
    }

    @Override
    public List<Declartion> getDatumDeclList() {
        return body.getDatumDeclList();
    }
}
