/*
 * Copyright 2003 Systematic Solutions Corporation, All Rights Reserved.
 *
 * This software is the proprietary information of Systematic Solutions Corporation.
 * Use is subject to license terms.
 *
 */
package com.jkc.roml.lang;

/**
 * ModelElements that are referenceable by name must implement Namespace.
 *
 * @author Jim Coles
 * @version 1.0
 */
public interface Namespace
{
  //----------------------------------------------------------------------------
  // Private instance vars
  //----------------------------------------------------------------------------

  //----------------------------------------------------------------------------
  // Public methods - accessors, mutators, other
  //----------------------------------------------------------------------------
  public String getCodeName();

  //----------------------------------------------------------------------------
  // Private methods
  //----------------------------------------------------------------------------

}
