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
 * The simple name portion of a meta object definition.
 *
 * @author Jim Coles
 */
public class NamePart extends AbstractModelElement implements Comparable<NamePart> {

    private String text;

    NamePart(SourceInfo sourceInfo, String text) {
        super(sourceInfo);
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public int compareTo(NamePart o) {
        return text.compareTo(o.getText());
    }
}
