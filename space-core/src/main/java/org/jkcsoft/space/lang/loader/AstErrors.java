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

import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.runtime.SpaceX;

import java.io.File;
import java.util.*;

/**
 *
 * @author Jim Coles
 */
public class AstErrors {

    private File parseFile;
    private Set<AstErrors> nestedErrors;

    private List<AstLoadError> allLoadErrors;
    private List<AstLoadError> syntaxErrors;

    public AstErrors(File parseFile) {
        this.parseFile = parseFile;
    }

    public void add(AstLoadError astLoadError) {
        if (allLoadErrors == null)
            allLoadErrors = new LinkedList<>();
        allLoadErrors.add(astLoadError);
        //
        if (astLoadError.getType() == AstLoadError.Type.SYNTAX) {
            if (syntaxErrors == null)
                syntaxErrors = new LinkedList<>();

            syntaxErrors.add(astLoadError);
        }
    }

    public boolean hasSyntaxErrors() {
        return syntaxErrors != null && syntaxErrors.size() > 0;
    }

    public List<AstLoadError> querySyntaxErrors() {
        return syntaxErrors;
    }

    public void addChildFileErrors(AstErrors child) {
        if (nestedErrors == null)
            nestedErrors = new HashSet<>();
        nestedErrors.add(child);
    }

    public void checkErrors() {
        if (hasSyntaxErrors()) {
            System.err.println(Strings.buildNewlineList(syntaxErrors));
            throw new SpaceX("found " + syntaxErrors.size() + " loader errors");
        }
    }

    public List<AstLoadError> getAllErrors() {
        return allLoadErrors;
    }
}
