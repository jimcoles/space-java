/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.metametamodel;

/**
 *
 */
public enum  PersState  {
    /**
     * Indicates that the object does not exist in the repository, but is in memory
     */
    NEW,
    /**
     * Indicates that the object is the same in memory as it is in the data store.
     */
    UNMODIFIED,
    /**
     * Indicates that the object has been altered in memory from what is in the data store.
     */
    MODIFIED,
    /**
     * Indicates that the object has only been partially loaded into memory.
     */
    PARTIAL,
    /**
     * Indicates that the object is in the database, but should be deleted.
     */
    DELETED,
    /**
     * Indicates that the object has not yet been loaded from the data store
     */
    UNINITED
}