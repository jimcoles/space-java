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

import org.jkcsoft.space.lang.metameta.MetaType;

/**
 * @author Jim Coles
 */
public class Language {

    public static final Language SPACE = new Language("space", "Space", "space");
    public static final Language JAVA = new Language("java", "Java", "java");

    private String codeName;
    private String displayName;
    private String fileExt;

    public Language(String codeName, String displayName, String fileExt) {
        this.codeName = codeName;
        this.displayName = displayName;
        this.fileExt = fileExt;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getFileExt() {
        return fileExt;
    }
}
