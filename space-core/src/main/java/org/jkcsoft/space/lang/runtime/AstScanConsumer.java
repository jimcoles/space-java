/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.ast.ModelElement;

import java.util.function.Predicate;

/**
 * @author Jim Coles
 */
public interface AstScanConsumer {

    Predicate<ModelElement> getFilter();

    boolean upon(ModelElement astNode);

    void after(ModelElement astNode);

}
