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

/**
 * Abstract base class for model extractors, which are objects that know how to
 * build a .data.model.DataModel graph for a given type of DataSystem, e.g.,
 * from JDBC meta data, from an XML DTD or Schema, etc.
 * <p>
 * DataModels built by the extractor are just a first guess at a model.  Data
 * designers can reverse engineer the generated model to build a 'logical'
 * model.
 *
 * @author J. Coles
 * @version 1.0
 */
public class ModelExtractor {


    public ModelExtractor() {
    }


    /**
     *
     */

    public void buildModel() {
    }



}
