/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space;

import org.jkcsoft.space.lang.ast.AstFactory;
import org.jkcsoft.space.lang.ast.NSRegistry;
import org.jkcsoft.space.lang.ast.sji.SjiService;

/**
 * An object to act as a central factory for instance-controlled objects.
 *
 * @author Jim Coles
 */
public class SpaceHome {

    private static AstFactory astFactory = new AstFactory();
    private static NSRegistry nsRegistry = NSRegistry.getInstance();
    private static SjiService sjiBuilder = new SjiService();

    public static AstFactory getAstFactory() {
        return astFactory;
    }

    public static NSRegistry getNsRegistry() {
        return nsRegistry;
    }

    public static SjiService getSjiService() {
        return sjiBuilder;
    }
}
