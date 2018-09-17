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
import org.jkcsoft.space.lang.ast.ParseUnit;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * An AstLoader loads an AST corresponding to a file or files on the file system.
 * Other types of loaders might also evolve, e.g., load from binary.  Several
 * AstLoader's might be used in a given Space runtime instance.
 *
 * @author Jim Coles
 */
public interface AstLoader {

    /** A unique name suitable for lookup, logging and debugging. No spaces please. */
    String getName();

    /**
     * Recursively loads the specified source directory as a Space Directory.
     * Loads any child source files as {@link ParseUnit}s. Recursively loads child
     * directories, possibly by recursive calls to the same method.
     * The consumer of this loader is responsible for merging the provied Space
     * Directories into a final usable form.
     *
     * @param srcDir A file system directory containing source to load.
     * @return The Space Directory associated with the source directory.
     * @throws IOException
     */
    DirLoadResults loadDir(File srcDir) throws IOException;

    /**
     * Directly load specified file into the spaceDir.
     *
     * @param spaceDir
     * @param spaceSrcFile
     * @return The {@link ParseUnit} associated with spaceSrcFile.
     * @throws IOException
     */
    FileLoadResults loadFile(Directory spaceDir, File spaceSrcFile) throws IOException;

    /**
     * The most general load method for loaders that are not file system based, e.g.,
     * RDB, web service.
     *
     * @param path Could be URL-like in whatever format the loader needs to resolve its
     *             internal storage.
     * @return
     */
    Directory loadFromResource(String path);

}
