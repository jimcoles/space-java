/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.langmaps.umlish;

/**
 * UML: core.Attribute
 * UML semantics deviations:
 * UML has Attribute -> StructuralFeature -> Feature, which has visibility
 * and ownerScope.  Our scope is always 'instance'-level.  Our visibility
 * is always 'public'.  Therefore, we have Attribute -> ModelElement.
 * <p>
 * An Attribute is inherently scoped to (owned by) one and only one Class.  Could call this a
 * ClassAttribute, but the 'Class' part is implied.
 * <p>
 * Note, this means that an Attribute is NOT a Classifier, but rather a usage
 * of a Classifier by a Class.
 *
 * @author J. Coles
 * @version 1.0
 */
public class Attribute extends ModelElement {
    //----------------------------------------------------------------------------
     // Instance-level

//  private String _initValue;  // Expression (per UML doc)

    private Classifier _type;   //
    private Class _owner;  //


    public Attribute() {
    }




    public Class getOwner() {
        return _owner;
    }

    public void setOwner(Class owner) {
        _owner = owner;
    }

    /**
     * Getter for property _type.
     *
     * @return Value of property _type.
     */
    public Classifier getType() {
        return _type;
    }

    /**
     * Setter for property _type.
     *
     * @param _type New value of property _type.
     */
    public void setType(Classifier type) {
        _type = type;
    }

}