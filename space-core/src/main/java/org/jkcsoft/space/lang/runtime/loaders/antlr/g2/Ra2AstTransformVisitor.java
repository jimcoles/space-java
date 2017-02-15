/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.runtime.loaders.antlr.g2;

import org.jkcsoft.space.antlr.Space2Parser;
import org.jkcsoft.space.antlr.Space2ParserBaseVisitor;

/**
 *
 * @author Jim Coles
 */
public class Ra2AstTransformVisitor extends Space2ParserBaseVisitor<Void> {
    @Override
    public Void visitParseUnit(Space2Parser.ParseUnitContext ctx) {
        Space2Parser.SpaceDefnContext spaceDefnContext = ctx.spaceDefn();

        return super.visitParseUnit(ctx);
    }

}
