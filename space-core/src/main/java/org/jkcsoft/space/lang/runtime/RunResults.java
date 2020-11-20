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

import org.jkcsoft.space.lang.ast.FileSourceInfo;
import org.jkcsoft.space.lang.loader.AstFileLoadErrorSet;
import org.jkcsoft.space.lang.loader.AstLoadError;
import org.jkcsoft.space.lang.loader.DirLoadResults;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Jim Coles
 */
public class RunResults {

    private Map<File, AstFileLoadErrorSet> errorsBySrcFile = new HashMap<>();
    private List<AstLoadError> allErrors = new LinkedList<>();
    private SpaceX runtimeError;

    public Map<File, AstFileLoadErrorSet> getErrorsBySrcFile() {
        return errorsBySrcFile;
    }

    public int[] getLevelCounts() {
        int[] levelCounts = new int[AstLoadError.Level.values().length];
        allErrors.forEach(err -> levelCounts[err.getType().getLevel().ordinal()]++);
        return levelCounts;
    }

    public List<AstLoadError> getAllErrors() {
        return allErrors;
    }

    public void addSrcDirLoadResult(DirLoadResults dirLoadResults) {
        dirLoadResults.getErrorList().forEach(
            err -> addFileError(err)
        );
    }

    public void addFileError(AstLoadError err) {
        File file = getFile(err);
        getFileErrorSet(file).add(err);
        allErrors.add(err);
    }

    private AstFileLoadErrorSet getFileErrorSet(File file) {
        AstFileLoadErrorSet errorSet = errorsBySrcFile.get(file);
        if (errorSet == null) {
            errorSet = new AstFileLoadErrorSet(file);
            errorsBySrcFile.put(file, errorSet);
        }
        return errorSet;
    }

    private File getFile(AstLoadError astLoadError) {
        return astLoadError.getSourceInfo() instanceof FileSourceInfo ?
            ((FileSourceInfo) astLoadError.getSourceInfo()).getFile() : null;
    }

    public SpaceX getRuntimeError() {
        return runtimeError;
    }

    public void setRuntimeError(SpaceX runtimeError) {
        this.runtimeError = runtimeError;
    }
}
