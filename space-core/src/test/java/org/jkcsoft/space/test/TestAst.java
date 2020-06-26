/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
*/
package org.jkcsoft.space.test;

import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.instance.ObjectFactory;
import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.metameta.MetaType;
import org.jkcsoft.space.lang.runtime.AstUtils;
import org.jkcsoft.space.lang.runtime.Executor;
import org.junit.Test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jim Coles
 */
public class TestAst {

    @Test
    public void testBuildAndRunProgram() {
        AstFactory astFactory = new AstFactory();
        ProgSourceInfo si = new ProgSourceInfo();
        astFactory.newProgram(si, "(API Builder Program)");
        //
        ObjectFactory objBuilder = ObjectFactory.getInstance();
        TypeDefnImpl complexTypeImpl = astFactory.newTypeDefn(si, astFactory.newNamePart(si, "MyHelloSpace"));

        Directory astDirectory = astFactory.newAstDir(si, "TestAst");
        astDirectory.addParseUnit(astFactory.newParseUnit(si)).addTypeDefn(complexTypeImpl);
//        complexTypeImpl.setBody(astFactory.newTypeDefnBody(si));
        complexTypeImpl
            .addVariableDecl(
                astFactory.newVariableDecl(si, "myIntDim", astFactory.newTypeRef(si, NumPrimitiveTypeDefn.CARD)));
        complexTypeImpl
//            .setBody(astFactory.newTypeDefnBody(si))
            .addVariableDecl(astFactory.newVariableDecl(si, "myCharDim",
                                                        astFactory.newTypeRef(si, NumPrimitiveTypeDefn.CHAR)));
        SpaceFunctionDefn mainMethod = astFactory.newSpaceFunctionDefn(si, "main", null);
        complexTypeImpl.addFunctionDefn(mainMethod);
//        CharacterSequence arg1 = objBuilder.newCharacterSequence("Hello, Space!");
//        astFactory.getUserAstRoot().addObjectInstance(arg1, astFactory);

        ThisTupleExpr thisTupleExpr = astFactory.newThisExpr(si);

        ExpressionChain functionDefnRef = astFactory.newMetaRefChain(si, MetaType.FUNCTION, astFactory
            .newNameRefExpr(si, NSRegistry.getInstance().getTmpNs().getName()));

        AstUtils.addNewMetaRefParts(functionDefnRef, si, "test", "TestType", "testFunc");
        mainMethod.setStatementBlock(astFactory.newStatementBlock(si));
        mainMethod.getStatementBlock()
                  .addExpr(astFactory.newFunctionCallExpr(si).setFunctionRef(((SimpleNameRefExpr) functionDefnRef.getFirstPart())));

//        astFactory.newMetaObjectRefLiteral(null),
//            astFactory.newPrimLiteralExpr("Hello, Space!")

        //        spaceDefn.
//            .setContextSpaceDefn(new SpaceDefn() {
//                @Override
//                public VariableDefn addVariable(VariableDefn coordinateDefn) {
//                    return super.addVariable(coordinateDefn);
//                }
//            })

        Executor spex = Executor.getInstance(new TestExeSettings());

        try {
            List<AstLoadError> errors = new LinkedList<>();
            spex.linkAndCheckUnit(astDirectory.getParseUnits().iterator().next(), errors);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static class TestExeSettings implements Executor.ExeSettings {
        @Override
        public String getExeMain() {
            return null;
        }

        @Override
        public List<File> getSpaceDirs() {
            return null;
        }
    }
}
