/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.langmaps.umlish;

/**
 * Represents the completed edit state of an item at a given
 * instance.
 */
public final class EditMask {
    private int _mask = 0;

    /**
     * Private constructor
     */
    public EditMask() {

    }

    public EditMask setValue(int value) {
        _mask = value;
        return this;
    }

    public boolean exactly(int value) {
        return (_mask == value);
    }

    public boolean allTrue(int value) {
        return ((_mask & value) == value);
    }

    public boolean oneTrue(int value) {
        return (_mask & value) > 0;
    }

    public boolean inSync() {
        if (!oneTrue(EditState.NEW | EditState.DELETED | EditState.MODIFIED))
            return true;
        else
            return false;
    }
}