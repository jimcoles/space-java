/*
 * Copyright 2002 Systematic Solutions Corporation, All Rights Reserved.
 *
 * This software is the proprietary information of Systematic Solutions Corporation.
 * Use is subject to license terms.
 *
 */
package com.jkc.roml.dms.persist;

/**
 * The base class for persisters.  Defines basic scheme for loading and saving
 * object graphs based on object model information.  Uses classes in .data and 
 * .data.model extensively.
 *
 * In lieu of the usual IPersitable interface, this framework uses the 
 * <code>.data.IObjectRecord</code> interface to indicate persistability.
 * IObjectRecord does not have save() and load() methods and therefore does not
 * allow the option of bean-managed persistence.
 *
 * @author J. Coles
 * @version 1.0
 */
public class Persister 
{
  //----------------------------------------------------------------------------
  // Private instance vars
  //----------------------------------------------------------------------------

  //----------------------------------------------------------------------------
  // Constructor(s) (private, package, protected, public)
  //----------------------------------------------------------------------------

  /** Constructor... */
  public Persister () 
  {
  }

  //----------------------------------------------------------------------------
  // Public methods - accessors, mutators, other
  //----------------------------------------------------------------------------

  //---- <Accessors and Mutators> ----------------------------------------------

  //---- </Accessors and Mutators> ----------------------------------------------

  //----------------------------------------------------------------------------
  // Private methods
  //----------------------------------------------------------------------------
    
}
