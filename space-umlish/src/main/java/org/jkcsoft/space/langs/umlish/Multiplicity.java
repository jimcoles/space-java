/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.langmaps.umlish;

/**
 * UML 'ScopeKind' enumeration.
 */
public enum Multiplicity {
    M0_1 (true, false),
    M1_1 (false, false),
    M0_M (true, true),
    M1_M (false, true);

    private boolean _optional;
    private boolean _many;

    private Multiplicity(boolean opt, boolean many) {
        _optional = opt;
        _many = many;
    }

    public boolean isOptional() {
        return _optional;
    }

    public boolean isMany() {
        return _many;
    }
}