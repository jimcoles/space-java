/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.antlr.loaders;

import org.antlr.v4.runtime.tree.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jkcsoft.space.lang.ast.AstFactory;

/**
 *
 * @author Jim Coles
 */
public class SimpleVisitor extends AbstractParseTreeVisitor<AstFactory>
        implements ParseTreeVisitor<AstFactory>
{

    private static final Logger log = LoggerFactory.getLogger(SimpleVisitor.class.getSimpleName());

    private AstFactory astFactory = new AstFactory();


    @Override
    public AstFactory visit(ParseTree parseTree) {
        log.info("in visit: " + parseTree.getText());
        return super.visit(parseTree);
    }

    @Override
    public AstFactory visitChildren(RuleNode ruleNode) {
        log.info("in visitChildren: " + ruleNode);
        return super.visitChildren(ruleNode);
    }

    @Override
    public AstFactory visitTerminal(TerminalNode terminalNode) {
        log.info("in visitTerminal: " + terminalNode);
        return super.visitTerminal(terminalNode);
    }

    @Override
    public AstFactory visitErrorNode(ErrorNode errorNode) {
        log.info("in visitErrorNode: " + errorNode);
        return super.visitErrorNode(errorNode);
    }

    @Override
    protected AstFactory defaultResult() {
        return astFactory;
    }
}
