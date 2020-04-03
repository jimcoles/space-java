/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime.typecasts;

import org.jkcsoft.space.lang.instance.CharacterSequence;
import org.jkcsoft.space.lang.runtime.Executor;

/**
 * Implement transforms from one type to another.
 *
 * @author Jim Coles
 */
public class CastTransforms {

    public String charSequenceToString(CharacterSequence characterSequence) {
        return characterSequence.toString();
    }

    public CharacterSequence stringToCharSeq(Executor.EvalContext evalContext, String jString) {
        return evalContext.newCharSequence(jString);
    }
}
