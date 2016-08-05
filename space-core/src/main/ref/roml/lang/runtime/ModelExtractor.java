/*
 * Copyright 2002 Systematic Solutions Corporation, All Rights Reserved.
 *
 * This software is the proprietary information of Systematic Solutions Corporation.
 * Use is subject to license terms.
 *
 */
package com.jkc.roml.dms;

/**
 * Abstract base class for model extractors, which are objects that know how to
 * build a .data.model.DataModel graph for a given type of DataSystem, e.g., 
 * from JDBC meta data, from an XML DTD or Schema, etc.
 * 
 * DataModels built by the extractor are just a first guess at a model.  Data
 * designers can reverse engineer the generated model to build a 'logical' 
 * model.
 *
 * @author J. Coles
 * @version 1.0
 */
public class ModelExtractor 
{
  //----------------------------------------------------------------------------
  // Private instance vars
  //----------------------------------------------------------------------------

  //----------------------------------------------------------------------------
  // Constructor(s) (private, package, protected, public)
  //----------------------------------------------------------------------------

  public ModelExtractor ()
  {
  }

  //----------------------------------------------------------------------------
  // Public methods - accessors, mutators, other
  //----------------------------------------------------------------------------
  
  /**
   *
   */
  
  public void buildModel( )
  {
  }
  
  //---- <Accessors and Mutators> ----------------------------------------------

  //---- </Accessors and Mutators> ----------------------------------------------

  //----------------------------------------------------------------------------
  // Private methods
  //----------------------------------------------------------------------------
    
}
