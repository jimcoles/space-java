/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast.persist;

import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.AbstractModelElement;
import org.jkcsoft.space.lang.ast.ModelElement;
import org.jkcsoft.space.lang.runtime.AstScanConsumer;
import org.jkcsoft.space.lang.runtime.AstUtils;

import java.util.function.Predicate;

/**
 * Visits every AST node writes out to a simple text format that looks
 * a bit like JSON or GPB text format.
 *
 * @author Jim Coles
 */
public class SimpleTextWriter implements AstWriter, AstScanConsumer {

    private final StringBuilder sb = new StringBuilder();

    @Override
    public Predicate<ModelElement> getFilter() {
        return null;
    }

    @Override
    public boolean upon(ModelElement astNode) {
        String indent = Strings.multiplyString("\t", astNode.getTreeDepth());
        sb.append(JavaHelper.EOL)
          .append(indent).append("(").append(astNode.toString());
        return true;
    }

    @Override
    public void after(ModelElement astNode) {
        if (astNode.hasChildren()) {
            String indent = Strings.multiplyString("\t", astNode.getTreeDepth());
            sb.append(JavaHelper.EOL).append(indent);
        }
        sb.append(")");
    }

    public StringBuilder getSb() {
        return sb;
    }

    @Override
    public void writeAst(AbstractModelElement rootElem) {
        AstUtils.walkAstDepthFirst(rootElem, this);
    }
}
