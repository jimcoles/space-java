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
 * In Space, the {@link TimeType} is a special variant of a sequence.
 * @author Jim Coles
 */
public class TimeType extends SequenceTypeDefn {

    TimeType(SourceInfo sourceInfo, DatumType containedElementType) {
        super(sourceInfo, containedElementType);
    }

}
