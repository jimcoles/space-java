/*
 * Copyright 2002 Systematic Solutions Corporation, All Rights Reserved.
 *
 * This software is the proprietary information of Systematic Solutions Corporation.
 * Use is subject to license terms.
 *
 */
package com.jkc.roml.testers;

import com.jkc.dms.data.DataSystemManager;


/**
 *
 * @author Jim Coles
 * @version 1.0
 */
public class DmsTester extends com.jkc.testers.Tester
{
  public static void main(String args[])
  {
    new DmsTester().instMain(args);
  }
  //----------------------------------------------------------------------------
  // Private instance vars
  //----------------------------------------------------------------------------

  //----------------------------------------------------------------------------
  // Constructor(s) (private, package, protected, public)
  //----------------------------------------------------------------------------

  /** Constructor... */
  private DmsTester () 
  {
    super();
  }

  public void test () throws java.lang.Exception
  {
    DataSystemManager dm = new DataSystemManager();
    dm.export();
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
