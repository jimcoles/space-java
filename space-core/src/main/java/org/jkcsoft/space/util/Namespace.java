/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
*/
package org.jkcsoft.space.util;

/**
 * @author Jim Coles
 */
public class Namespace {

    private String fullPath = "";
    private String[] paths;
    private char PATH_DELIM = '.';

    public Namespace(String fullPath) {
        this.fullPath = fullPath;
        this.paths = fullPath.split("\\.");
    }

    public Namespace(String[] paths) {
        this.paths = paths;
        StringBuilder sb = new StringBuilder();
        for (int idxPath = 0; idxPath < paths.length; idxPath++) {
            if (idxPath != 0) {
                sb.append(PATH_DELIM);
            }
            sb.append(paths[idxPath]);
        }
        fullPath = sb.toString();
    }

    public String[] getPaths() {
        return paths;
    }

    public String getLast() {
        return paths[paths.length - 1];
    }

    public int getSize() {return paths.length; }

    public String subPath(int idxFirst, int idxLast) {
        StringBuilder sb = new StringBuilder();
        for (int idxPart = idxFirst; idxPart <= idxLast; idxPart++) {
            sb.append(paths[idxPart]);
            if (idxPart < idxLast)
                sb.append(".");
        }
        return sb.toString();
    }

    public String getAbsolutePath() {
        return fullPath;
    }
}
