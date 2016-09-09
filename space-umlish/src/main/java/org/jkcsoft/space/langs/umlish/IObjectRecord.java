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
 * Analogous to an XML org.w3c.xml.dom.Element.  This interface is intended to support
 * generic object persistence logic include traversal to associated objects
 * and generic getting and setting of attribute values.
 * <p>
 * Should be implemented by all SSC DMS internal peristable classes and
 * by a generic ObjectRecord class used to hold data source data during
 * primary data operations such as diff, reconcile, sync.
 * <p>
 * Parent entity is a RecordSet which is analogous to an xml.Document(Node).
 * <p>
 * This is NOT the query interface for finding sub-objects.
 */
public interface IObjectRecord {
    public void setParentObject(IObjectRecord parent) throws Exception;

    public IObjectRecord getParentObject() throws Exception;

    /**
     * Generic getter
     */
    public Object get(String attrCodeName) throws Exception;

    /**
     * Generic setter
     */
    public void set(String attrCodeName, Object value) throws Exception;

    // TODO: Varous overloads of the getxxx( ) methods are possible:
    //       - by assoc longId (long, Oid)
    //       - by assoc codeName (String)
    //       - by xpath query string (String)

    public Identifier getAssocRef(String assocCodeName) throws Exception;
}