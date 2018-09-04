/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast.sji;

import org.jkcsoft.space.lang.ast.DatumType;
import org.jkcsoft.space.lang.ast.LoadState;

/**
 * @author Jim Coles
 */
public class SjiTypeMapping {

    private Class javaClass; // the key
    private DatumType spaceWrapper; // the target
    private LoadState state;

    public SjiTypeMapping(Class javaClass) {
        this.javaClass = javaClass;
    }

    public Class getJavaClass() {
        return javaClass;
    }

    public DatumType getSpaceWrapper() {
        return spaceWrapper;
    }

    public void setSpaceWrapper(DatumType resolvedType) {
        this.spaceWrapper = resolvedType;
    }

    public LoadState getState() {
        return state;
    }

    public void setState(LoadState state) {
        this.state = state;
    }

    public boolean isPrimitive() {
        return spaceWrapper != null && spaceWrapper.isPrimitive();
    }

}
