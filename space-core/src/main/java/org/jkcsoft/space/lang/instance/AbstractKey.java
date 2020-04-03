/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

/**
 * An {@link AbstractKey} exists primarily to represent the abstract notion of a 'character'.
 * It is key that does not reference a concrete Space Object, but rather an abstract, well-known,
 * externally defined thing.  In the case of a 'character', the keys maps to a well-known
 * character sets such as UTF-16 or UTF-8.
 *
 * @author Jim Coles
 */
public interface AbstractKey extends KeyValue {
}
