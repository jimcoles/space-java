/*
 * Copyright 2002 Systematic Solutions Corporation, All Rights Reserved.
 *
 * This software is the proprietary information of Systematic Solutions Corporation.
 * Use is subject to license terms.
 *
 */
package com.jkc.roml.lang.runtime;

import com.jkc.roml.lang.ModelElement;
import com.jkc.roml.lang.Action;
import com.jkc.roml.lang.RomlProgram;
import com.jkc.roml.lang.Relation;

/**
 * In pattern terms, the command processor.  Knows how to run a RomlProgram
 * including executing Queries.
 *
 * @author Jim Coles
 * @version 1.0
 */
public abstract class ExprProcessor
{
  //----------------------------------------------------------------------------
  // Private instance vars
  //----------------------------------------------------------------------------

  //----------------------------------------------------------------------------
  // Constructor(s) (private, package, protected, public)
  //----------------------------------------------------------------------------


  //----------------------------------------------------------------------------
  // Public methods - accessors, mutators, other
  //----------------------------------------------------------------------------

  /**
   * The starting point for doing something. The exec method works iteratively
   * upon an ActionSequence.
   *
   * TODO: This is an excellent point to do some trace logging.
   */
//  abstract public ModelElement exec(ModelElement action) throws SpaceX;

  //----------------------------------------------------------------------------
  // Private methods
  //----------------------------------------------------------------------------
    
}
