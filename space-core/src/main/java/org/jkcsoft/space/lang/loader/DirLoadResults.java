/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.loader;

import org.jkcsoft.space.lang.ast.Directory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jim Coles
 */
public class DirLoadResults {

    private File srcDir;
    private Directory spaceRootDir;
    private List<AstLoadError> errorList;

    public DirLoadResults(File srcDir) {
        this.srcDir = srcDir;
    }

    public File getSrcDir() {
        return srcDir;
    }

    public Directory getSpaceRootDir() {
        return spaceRootDir;
    }

    public void setSpaceRootDir(Directory spaceRootDir) {
        this.spaceRootDir = spaceRootDir;
    }

    public boolean hasErrors() {
        return errorList != null && errorList.size() > 0;
    }

    public List<AstLoadError> getErrorList() {
        initErrorList();
        return errorList;
    }

    public void addError(AstLoadError error) {
        initErrorList();
        this.errorList.add(error);
    }

    private void initErrorList() {
        if (errorList == null)
            errorList = new LinkedList<>();
    }

    @Override
    public String toString() {
        return "DirLoadResults{" +
            "srcDir=" + srcDir +
            ", spaceRootDir=" + spaceRootDir +
            ", errors=" + (errorList != null ? errorList.size() : 0)+
            '}';
    }
}
