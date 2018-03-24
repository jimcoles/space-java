/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.ast.SourceInfo;
import org.jkcsoft.space.lang.instance.FunctionCallContext;

import java.util.Stack;

/**
 * Holds detailed error info.  Used by {@link SpaceX} and by loaders
 * and linkers.
 *
 * @author Jim Coles
 */
public class RuntimeError {

    private Stack<FunctionCallContext> stackTrace;
    private int errorCode;
    private SourceInfo sourceInfo;
    private String message;

    public RuntimeError(Stack<FunctionCallContext> stackTrace, int errorCode, String message) {
        this.stackTrace = stackTrace;
        this.message = message;
        this.errorCode = errorCode;
    }

    public RuntimeError(SourceInfo sourceInfo, int errorCode, String message) {
        this.sourceInfo = sourceInfo;
        this.message = message;
        this.errorCode = errorCode;
    }

    /** Future best practice: use error code to lookup the message text which is templated to
     * take arguments args.
     * @param errorId Specific error id.
     * @param args Arguments, often just Strings, that get inserted into error message.
     */
    public RuntimeError(int errorId, Object... args) {

    }

    public Stack<FunctionCallContext> getSpaceTrace() {
        return stackTrace;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public SourceInfo getSourceInfo() {
        return sourceInfo;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return (errorCode > 0 ? "<" + errorCode + ">" : "") +
            (sourceInfo != null ? sourceInfo.toBriefString() + " " : "") + message;
    }

}
