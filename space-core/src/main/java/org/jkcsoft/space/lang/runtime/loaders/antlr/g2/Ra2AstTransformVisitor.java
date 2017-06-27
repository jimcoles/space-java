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

import org.jkcsoft.space.antlr.SpaceParser;
import org.jkcsoft.space.antlr.SpaceParserBaseVisitor;

/**
 *
 * @author Jim Coles
 */
public class Ra2AstTransformVisitor extends SpaceParserBaseVisitor<Void> {
    @Override
    public Void visitParseUnit(SpaceParser.ParseUnitContext ctx) {
        SpaceParser.SpaceDefnContext spaceDefnContext = ctx.spaceDefn();

        return super.visitParseUnit(ctx);
    }

}
