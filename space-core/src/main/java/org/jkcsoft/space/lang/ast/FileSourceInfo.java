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

import java.io.File;

/**
 * @author Jim Coles
 */
public class FileSourceInfo implements SourceInfo {

    private File file;
    private FileCoord start;
    private FileCoord end;

    public FileSourceInfo(File file, FileCoord start, FileCoord end) {
        this.file = file;
        this.start = start;
        this.end = end;
    }

    @Override
    public FileCoord getStart() {
        return start;
    }

    @Override
    public FileCoord getStop() {
        return end;
    }

    @Override
    public String toString() {
        return file.getName() + ":[" + start + ".." + end + "]";
    }

    @Override
    public String toBriefString() {
        return file.getName() + ":[" + start + "]";
    }

}
