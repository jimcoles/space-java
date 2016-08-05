/*
 * Copyright 2002 Systematic Solutions Corporation, All Rights Reserved.
 *
 * This software is the proprietary information of Systematic Solutions Corporation.
 * Use is subject to license terms.
 *
 */
/*
 * Copyright 2002 Systematic Solutions Corporation, All Rights Reserved.
 *
 * This software is the proprietary information of Systematic Solutions Corporation.
 * Use is subject to license terms.
 *
 */
package com.jkc.roml.lang;

/**
 * Instance-level holder of sequence/list of items at the same meta level.
 * A List is a Relation that implicitly maintains a sequence number from
 * 1 to n for all Tuples.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class List extends Relation
{
  //----------------------------------------------------------------------------
  // Private instance vars
  //----------------------------------------------------------------------------

  //----------------------------------------------------------------------------
  // Constructor(s) (private, package, protected, public)
  //----------------------------------------------------------------------------

  /** Constructor... */
  public List()
  {
  }

  //----------------------------------------------------------------------------
  // Public methods - accessors, mutators, other
  //----------------------------------------------------------------------------
  Iterator iterator() {
      return null;
  }

  //----------------------------------------------------------------------------
  // Private methods
  //----------------------------------------------------------------------------

}
