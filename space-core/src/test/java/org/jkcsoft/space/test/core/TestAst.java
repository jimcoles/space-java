/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.test.core;

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
        astFactory.newProgram("(API Builder Program)");
        //
        ObjectFactory objBuilder = ObjectFactory.getInstance();
        TypeDefnImpl complexTypeImpl = astFactory.newTypeDefn("MyHelloSpace");

        Directory astDirectory = astFactory.newAstDir("TestAst");
        astDirectory.addParseUnit(astFactory.newParseUnit()).addTypeDefn(complexTypeImpl);
        complexTypeImpl.addVariableDecl(
            astFactory.newVariableDecl("myIntDim", complexTypeImpl, NumPrimitiveTypeDefn.CARD)
        );
        complexTypeImpl.addVariableDecl(
            astFactory.newVariableDecl("myCharDim", complexTypeImpl, NumPrimitiveTypeDefn.CHAR)
        );
        FunctionDefnImpl mainMethod = astFactory.newSpaceFunctionDefn("main", null);
        complexTypeImpl.addFunctionDefn(mainMethod);
//        CharacterSequence arg1 = objBuilder.newCharacterSequence("Hello, Space!");
//        astFactory.getUserAstRoot().addObjectInstance(arg1, astFactory);

        ThisTupleExpr thisTupleExpr = astFactory.newThisExpr();

        ExpressionChain<FunctionDefn> functionDefnRef = astFactory.newMetaRefChain(MetaType.FUNCTION, NSRegistry.NS_TMP);
        astFactory.addNewMetaRefParts(functionDefnRef, SourceInfo.API, "test", "TestType", "testFunc");
        mainMethod.setStatementBlock(astFactory.newStatementBlock());
        mainMethod.getStatementBlock()
                  .addValueExpr(astFactory.newFunctionCallExpr().setFunctionRef(((SimpleNameRefExpr) functionDefnRef.getFirstPart())));

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
