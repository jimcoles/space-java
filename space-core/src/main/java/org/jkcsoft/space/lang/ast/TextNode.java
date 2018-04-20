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
 * @author Jim Coles
 */
public class TextNode extends ModelElement {

    private String text;

    TextNode(SourceInfo sourceInfo, String text) {
        super(sourceInfo);
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
