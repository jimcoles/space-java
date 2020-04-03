/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

/**
 * A sequence of {@link ReferenceValueHolder}s. Used to model domains that are naturally
 * described by sequences such as Documents as sequences of sections, lines, or XML or HTML
 * documents, music playlists, certain hierarchies, etc.
 *
 * <p>Sequences may be persisted.
 *
 * <p>Unlike the RDB model, sequences are not modeled as, and are not equivalent to, sorted sets,
 * although, of course, the one may be transformed into the other.
 *
 * @author Jim Coles
 */
public interface TupleSequence extends TupleCollection {

}
