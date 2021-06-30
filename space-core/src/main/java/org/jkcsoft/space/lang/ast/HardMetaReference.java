/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2021 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * A direct link to another {@link ModelElement} probably always known at the
 * point the AST is built. Like a Unix hard link (as opposed to symbolic link).
 * Used for referencing unnamed elements.
 *
 * @author Jim Coles
 */
public class HardMetaReference<T extends ModelElement> extends AbstractModelElement {

    private T target;

    public HardMetaReference(SourceInfo sourceInfo, T target) {
        super(sourceInfo);
        this.target = target;
    }

    public T getTarget() {
        return target;
    }

}
