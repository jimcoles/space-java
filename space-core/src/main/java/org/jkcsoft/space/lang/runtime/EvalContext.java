/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.instance.*;

/**
 * Everything needed by an eval( ) method to resolve values in the lexical
 * context chain.
 *
 * @author Jim Coles
 */
public interface EvalContext {

    StaticExeContext getStaticExeContext();

    //

    int callStackSize();

    void push(FunctionCallContext functionCallContext);

    FunctionCallContext peekStack();

    FunctionCallContext popCallStack();

    //

    RuntimeError newRuntimeError(String msg);

    CharacterSequence newCharSequence(String jString);

    ValueHolder newVoidHolder();

    SpaceObject dereferenceByOid(SpaceOid javaValue);

    Value newCharacterSequence(String valueExpr);

}
