/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.runtime;

import org.jkcsoft.space.lang.ast.ModelElement;
import org.jkcsoft.space.lang.instance.Space;
import org.jkcsoft.space.lang.ast.SpaceProgram;
import org.jkcsoft.space.lang.runtime.loaders.xml.XmlLoader;

import java.io.File;
import java.io.FileInputStream;
import java.util.Map;

/**
 * The top-level executive for Space.  It manages interatctions between second-level
 * elements including XmlLoader, ExprProcessor, Querier.
 *
 * @author J. Coles
 * @version 1.0
 */
public class Executor extends ExprProcessor {

    /**
     *
     */
    public void main(String[] args) {
        try {
            String stFilePath = args[0];
            File file = new File(stFilePath);
            if (!file.exists()) {
                throw new Exception("Input file [" + stFilePath + "] does not exist.");
            }
            XmlLoader loader = new XmlLoader(new FileInputStream(file));
            SpaceProgram program = loader.load();
            Executor exec = new Executor();
            exec.eval(program);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }


    // The symbol tables for 'instance' objects associated with the running program.
    // This is added to as the program runs.
    // Properties:
    private Space _programObjects;

    /**
     * Mapping from expression type to expression handler. Some handlers will be
     * Space standard, some will be user-provided.
     */
    private Map _exprProcessors = null;

    //----------------------------------------------------------------------------
    // Constructor(s)
    //----------------------------------------------------------------------------

    public Executor() {

    }

    //----------------------------------------------------------------------------
    // Public methods
    //----------------------------------------------------------------------------

    public ModelElement eval(ModelElement action) {
        return null;
    }

    /**
     * Evaluates a SpaceProgram.
     */
    public ModelElement eval(SpaceProgram program) throws Exception {

        return null;
    }



}
