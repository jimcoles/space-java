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

import org.jkcsoft.space.lang.ast.LinkState;

/**
 * @author Jim Coles
 */
public class SjiTypeMapping {

    private Class javaClass; // the key
    private SjiTypeDefn sjiProxy; // the target
    private LinkState state = LinkState.INITIALIZED;

    public SjiTypeMapping(Class javaClass) {
        this.javaClass = javaClass;
    }

    public Class getJavaClass() {
        return javaClass;
    }

    public SjiTypeDefn getSjiProxy() {
        return sjiProxy;
    }

    public void setSjiProxy(SjiTypeDefn resolvedType) {
        this.sjiProxy = resolvedType;
    }

    public LinkState getState() {
        return state;
    }

    public void setState(LinkState state) {
        this.state = state;
    }

    public boolean isPrimitive() {
        return sjiProxy != null && sjiProxy.isPrimitive();
    }

    @Override
    public String toString() {
        return "SJI mapping for Java class ["+javaClass+"] " +
            "state=" + getState() + " maps to ["+ sjiProxy +"]";
    }
}
