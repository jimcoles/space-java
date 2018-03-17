/*
 * Copyright 2002 Systematic Solutions Corporation, All Rights Reserved.
 *
 * This software is the proprietary information of Systematic Solutions Corporation.
 * Use is subject to license terms.
 *
 */
package com.jkc.roml.lang;

/**
 * Captures a wide range of relationships such as one-to-many, recursive.
 * @author Jim Coles
 * @version 1.0
 */
public class AssociationDefn
{
  //----------------------------------------------------------------------------
  // Private instance vars
  //----------------------------------------------------------------------------
  private Type _child;
  private Type _parent;
  
  //----------------------------------------------------------------------------
  // Constructor(s) (private, package, protected, public)
  //----------------------------------------------------------------------------

  /** Constructor... */
  public AssociationDefn ()
  {
  }

  //----------------------------------------------------------------------------
  // Public methods - accessors, mutators, other
  //----------------------------------------------------------------------------

  //---- <Accessors and Mutators> ----------------------------------------------

  /** In ROML, this expression would go in an Equation expression. */
  public boolean isRecursive() { return _child == _parent; }
  
  //---- </Accessors and Mutators> ----------------------------------------------

  //----------------------------------------------------------------------------
  // Private methods
  //----------------------------------------------------------------------------
    
}
