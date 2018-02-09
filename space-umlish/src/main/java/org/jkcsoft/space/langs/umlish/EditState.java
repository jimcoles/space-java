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
 * Typesafe constants relating the persistence state of an item.
 * The integers must be 1...n.
 */
public final class EditState {
    public static final int NEW = 1;
    public static final int MODIFIED = 2;
    public static final int DELETED = 4;
    public static final int LOADED = 8;
}