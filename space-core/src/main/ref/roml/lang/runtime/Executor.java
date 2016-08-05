/*
 * Copyright 2002 Systematic Solutions Corporation, All Rights Reserved.
 *
 * This software is the proprietary information of Systematic Solutions Corporation.
 * Use is subject to license terms.
 *
 */
package com.jkc.roml.lang.runtime;

import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Map;
import java.util.List;

/**
 * The top-level executive for ROML.  It manages interatction between second-level
 * elements including Loader, ExprProcessor, Querier.
 *
 * @author J. Coles
 * @version 1.0
 */
public class Executor extends ExprProcessor
{
  //----------------------------------------------------------------------------
  // Class-level members
  //----------------------------------------------------------------------------


  /**
   * .Executor roml-file-path
   * @param args
   */
  public void main(String[] args)
  {
    try {
      String stFilePath = args[0];
      File file = new File(stFilePath);
      if (!file.exists()) {
        throw new Exception("Input file ["+stFilePath+"] does not exist.");
      }
      Loader loader = new Loader(new FileInputStream(file));
      RomlProgram program = loader.load();
      Executor exec = new Executor();
      exec.eval(program);
//      exec.eval();
    }
    catch (Throwable th) {
      th.printStackTrace();
    }
  }

  //----------------------------------------------------------------------------
  // Private instance vars
  //----------------------------------------------------------------------------

  // The symbol tables for 'instance' objects associated with the running program.
  // This is added to as the program runs.
  // Properties:
  private Relation _programObjects;

  /**
   Mapping from expression type to expression handler. Some handlers will be
   ROML standard, some will be user-provided.
  */
  private Map _exprProcessors = null;
  
  //----------------------------------------------------------------------------
  // Constructor(s)
  //----------------------------------------------------------------------------

  /** Constructor... */
  public Executor()
  {

  }

  //----------------------------------------------------------------------------
  // Public methods
  //----------------------------------------------------------------------------

  public ModelElement eval(ModelElement action)
  {
    return null;
  }

  /** Evaluates a RomlProgram. */
  public ModelElement eval(RomlProgram program) throws Exception
  {
    return null;
  }



  //----------------------------------------------------------------------------
  // Private methods
  //----------------------------------------------------------------------------
    
}
