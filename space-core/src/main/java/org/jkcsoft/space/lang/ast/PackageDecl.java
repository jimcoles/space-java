/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * Tells the Space runtime which Package a given element belongs to.
 *
 * @author Jim Coles
 */
public class PackageDecl extends AbstractModelElement {

    private ExpressionChain packageRef;

    PackageDecl(SourceInfo sourceInfo, ExpressionChain packageRef) {
        super(sourceInfo);
        this.packageRef = packageRef;
    }

    public ExpressionChain getPackageRef() {
        return packageRef;
    }
}
