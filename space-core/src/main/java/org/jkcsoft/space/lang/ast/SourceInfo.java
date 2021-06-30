/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

/**
 * The start point
 * @author Jim Coles
 */
public interface SourceInfo {

    SourceInfo INTRINSIC = new IntrinsicSourceInfo();
    /** Used when AST is built from a Java code. */
    SourceInfo API = new ProgSourceInfo();
    /** Element corresponds to more than one source element. */
    SourceInfo COMPOSITION = new SourceInfo() {
        @Override
        public FileCoord getStart() {
            return null;
        }

        @Override
        public FileCoord getStop() {
            return null;
        }

        @Override
        public String toString() {
            return "(synthetic)";
        }
    };

    FileCoord getStart();

    FileCoord getStop();

    default String toBriefString() {
        return this.toString();
    }

    /** Primarily aimed at supporting IDE cursor placement and user location messages. */
    interface FileCoord {

        /** True if this coordinate is the start of a selection; false if stop. */
        boolean isStart();

        /** 1-based line number */
        int getLine();

        /** Semantic suitable for Java String.substring() calls without need for 1-off
         * tweaking arithmetic.
         * 0-based index of cursor position. If isStart = true,
         * the selection should include first char to right of index;
         * if false, should include last char to left of index. */
        int getCursorIndexInLine();
    }

}
