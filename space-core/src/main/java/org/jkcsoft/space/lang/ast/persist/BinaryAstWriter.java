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

import org.jkcsoft.space.lang.ast.ModelElement;
import org.jkcsoft.space.lang.runtime.AstScanConsumer;

import java.util.function.Predicate;

/**
 * Visits every AST node writes out in a terse binary format.
 *
 * @author Jim Coles
 */
public class BinaryAstWriter implements AstWriter, AstScanConsumer {

    @Override
    public Predicate<ModelElement> getFilter() {
        return null;
    }

    @Override
    public boolean upon(ModelElement astNode) {
        // TODO
        return true;
    }

    @Override
    public void after(ModelElement astNode) {
        // TODO
    }

    @Override
    public void writeAst(ModelElement rootElem) {
        //
    }
}
