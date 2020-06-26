/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * In Space, the {@link TimeType}, aka, "Space Time", is a special variant of a sequence.
 * The Space runtime guarantees that all operations get a unique {@link TimeType} value.
 * @author Jim Coles
 */
public class TimeType extends SequenceTypeDefn {

    TimeType(SourceInfo sourceInfo, TypeDefn containedElementType) {
        super(sourceInfo, containedElementType);
    }

}
