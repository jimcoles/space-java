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

import org.jkcsoft.java.beans.BeanDispatcher;

/**
 * UML Analogy: core.ModelElement.
 * <p>
 * Base class for all DataModel elements such as Class, Attr, etc.
 * <p>
 * By declaring ModelElement to implement IObjectRecord we are stating that
 * all SSC metamodel elements are instances of Class, even the Class itself.
 *
 * @author Jim Coles
 * @version 1.0
 */

public abstract class ModelElement implements IObjectRecord {
    //----------------------------------------------------------------------------
    // Private instance vars
    //----------------------------------------------------------------------------

    // delegate object(s)
    private static BeanDispatcher _beaner;

    // primitive state properties
    private Guid _guid;
    private Oid _oid;
    private String _friendlyName;
    private String _codeName;
    private String _description;

    // associations
    private Namespace _namespace;
//    private DataModel _dataModel;

    //----------------------------------------------------------------------------
    // Constructor(s) (private, package, protected, public)
    //----------------------------------------------------------------------------
    public ModelElement() {
        super();
    }

    //----------------------------------------------------------------------------
    // Public methods - accessors, mutators, other
    //----------------------------------------------------------------------------

    //---- <Accessors and Mutators> ----------------------------------------------

    public Guid getGuid() {
        return _guid;
    }

    public Oid getOid() {
        return _oid;
    }

    public String getFriendlyName() {
        return _friendlyName;
    }

    public String getCodeName() {
        return _codeName;
    }

    public String getDescription() {
        return _description;
    }

    public void setGuid(Guid guid) {
        _guid = guid;
    }

    public void setOid(Oid oid) throws Exception {
        _oid = oid;
    }

    public void setFriendlyName(String friendlyName) {
        _friendlyName = friendlyName;
    }

    public void setCodeName(String codeName) {
        _codeName = codeName;
    }

    public void setDescription(String description) {
        _description = description;
    }

    /**
     * Sets AttributeValue associated with attrCodeName.
     */
    public void set(String attrCodeName, Object value)
            throws Exception {
        _beaner.set(this, attrCodeName, value);
    }

    /**
     * Gets AttributeValue associated with attrCodeName.
     */
    public Object get(String attrCodeName)
            throws Exception {
        return _beaner.get(this, attrCodeName);
    }

    /**
     * Non-primitive attributes will be retrieved by id.  Consuming object
     * can dereference into an object.
     */
    public Identifier getAssocRef(java.lang.String str)
            throws Exception {
        return null;
    }

    public void setParentObject(IObjectRecord parent)
            throws Exception {
//        if (!(parent instanceof DataModel))
//            throw new Exception("Wrong data type in setParentObject().  Requires " + DataModel.class.getName());
//        _dataModel = (DataModel) parent;
    }

    public IObjectRecord getParentObject() {
//        return _dataModel;
        return null;
    }


    //---- </Accessors and Mutators> ----------------------------------------------

    //----------------------------------------------------------------------------
    // Private methods
    //----------------------------------------------------------------------------

}