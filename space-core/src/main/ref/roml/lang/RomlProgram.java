/*
 * Copyright 2003 Systematic Solutions Corporation, All Rights Reserved.
 *
 * This software is the proprietary information of Systematic Solutions Corporation.
 * Use is subject to license terms.
 *
 */
package com.jkc.roml.lang;

/**
 * Encapsulates an entire executable system as defined by ROML definition elements
 * (ModelElements) and associated instances.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class RomlProgram extends ModelElement
{
  //----------------------------------------------------------------------------
  // Private instance vars
  //----------------------------------------------------------------------------
  Relation _relationDefns;
  Relation _assocDefns;
  Relation _actionSequenceDefns;

  // TODO: indexes for fast lookup

  //----------------------------------------------------------------------------
  // Constructor(s) (private, package, protected, public)
  //----------------------------------------------------------------------------

  /** Constructor... */
  public RomlProgram()
  {
  }

  //----------------------------------------------------------------------------
  // Public methods - accessors, mutators, other
  //----------------------------------------------------------------------------


  //----------------------------------------------------------------------------
  // Private methods
  //----------------------------------------------------------------------------

}
