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

import org.apache.commons.collections.CollectionUtils;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.ast.FileSourceInfo;
import org.jkcsoft.space.lang.runtime.SpaceX;

import java.io.File;
import java.util.*;

/**
 *
 * @author Jim Coles
 */
public class AstFileLoadErrorSet {

    private File parseFileOrDir;
//    private Set<AstFileLoadErrors> childNodes;

    private List<AstLoadError> allLoadErrors;
    private List<AstLoadError> syntaxErrors;
    private int[] counts = new int[AstLoadError.Level.values().length];

    {
        for (AstLoadError.Level level : AstLoadError.Level.values()) {
            counts[level.ordinal()] = 0;
        }
    }

    public AstFileLoadErrorSet(File parseFileOrDir) {
        this.parseFileOrDir = parseFileOrDir;
    }

    public File getParseFileOrDir() {
        return parseFileOrDir;
    }

    public void add(AstLoadError astLoadError) {
        initAllErrorsList();
        //
        allLoadErrors.add(astLoadError);
        counts[astLoadError.getType().getLevel().ordinal()]++;
        //
        if (astLoadError.getType() == AstLoadError.Type.SYNTAX) {
            if (syntaxErrors == null)
                syntaxErrors = new LinkedList<>();

            syntaxErrors.add(astLoadError);
        }
    }

    private void initAllErrorsList() {
        if (allLoadErrors == null)
            allLoadErrors = new LinkedList<>();
    }

    public boolean hasSyntaxErrors() {
        return syntaxErrors != null && syntaxErrors.size() > 0;
    }

    public List<AstLoadError> querySyntaxErrors() {
        return syntaxErrors;
    }

    public void checkErrors() {
        if (hasSyntaxErrors()) {
            System.err.println(Strings.buildNewlineList(syntaxErrors));
            throw new SpaceX("found " + syntaxErrors.size() + " loader errors");
        }
    }

    public List<AstLoadError> getAllErrors() {
        initAllErrorsList();
        return allLoadErrors;
    }

    public boolean hasErrors() {
        return allLoadErrors != null && allLoadErrors.size() > 0;
    }

    public String getSummary() {
        return "errors (" + counts[AstLoadError.Level.ERROR.ordinal()] + ") warnings (" +
            counts[AstLoadError.Level.WARN.ordinal()] + ")";
    }

    public Collection getErrorsAtLevel(AstLoadError.Level errorLevel) {
        return CollectionUtils.select(allLoadErrors, err -> ((AstLoadError) err).getType().getLevel() == errorLevel);
    }
}
