/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.metametamodel;

import java.util.List;

/**
 * The Class meta element, i.e., a Table, Entity, etc.
 *
 * @author J. Coles
 * @version 1.0
 */
public class Class extends Classifier {
    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------
    // primitive state
    private boolean _isLeaf = false;
    private boolean _isRoot = false;

    // child list(s)
    private List _attributeList = null;

    //----------------------------------------------------------------------------
    //----------------------------------------------------------------------------
    public Class() {
    }

    //----------------------------------------------------------------------------
    // Public methods - accessors, mutators, other
    //----------------------------------------------------------------------------

    //---- <Accessors and Mutators> ----------------------------------------------

    public boolean isLeaf() {
        return _isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        _isLeaf = isLeaf;
    }

    public boolean isRoot() {
        return _isRoot;
    }

    /**
     * Getter for property _attributeList.
     *
     * @return Value of property _attributeList.
     */
    public List getAttributeList() {
        return _attributeList;
    }

    /**
     * Setter for property _attributeList.
     *
     * @param _attributeList New value of property _attributeList.
     */
    public void setAttributeList(List attributeList) {
        _attributeList = attributeList;
    }

    /**
     * Setter for internal list.
     */
    public void addAttribute(Attribute attrib) {
        _attributeList.add(attrib);
    }

    //---- </Accessors and Mutators> ----------------------------------------------

}