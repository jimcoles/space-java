/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime.loaders.antlr.g2;

import org.antlr.v4.runtime.tree.*;
import org.apache.log4j.Logger;
import org.jkcsoft.space.lang.ast.AstBuilder;

/**
 *
 * @author Jim Coles
 */
public class SimpleVisitor extends AbstractParseTreeVisitor<AstBuilder>
        implements ParseTreeVisitor<AstBuilder>
{

    private static final Logger log = Logger.getLogger(SimpleVisitor.class.getSimpleName());

    private AstBuilder astBuilder = new AstBuilder();


    @Override
    public AstBuilder visit(ParseTree parseTree) {
        log.info("in visit: " + parseTree.getText());
        return super.visit(parseTree);
    }

    @Override
    public AstBuilder visitChildren(RuleNode ruleNode) {
        log.info("in visitChildren: " + ruleNode);
        return super.visitChildren(ruleNode);
    }

    @Override
    public AstBuilder visitTerminal(TerminalNode terminalNode) {
        log.info("in visitTerminal: " + terminalNode);
        return super.visitTerminal(terminalNode);
    }

    @Override
    public AstBuilder visitErrorNode(ErrorNode errorNode) {
        log.info("in visitErrorNode: " + errorNode);
        return super.visitErrorNode(errorNode);
    }

    @Override
    protected AstBuilder defaultResult() {
        return astBuilder;
    }
}
