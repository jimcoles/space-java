/*
 * Copyright 2002 Systematic Solutions Corporation, All Rights Reserved.
 *
 * This software is the proprietary information of Systematic Solutions Corporation.
 * Use is subject to license terms.
 *
 */
package com.jkc.roml.lang;

import java.util.Map;
import java.util.List;

/**
 * Conceptually, an element of a Relation. Much like a row in a JDBC recordset.<br><br>
 *
 * Physcially,
 *
 * @author Jim Coles
 * @version 1.0
 */
public class Tuple
{
  //----------------------------------------------------------------------------
  // Private instance vars
  //----------------------------------------------------------------------------
  Relation _relation;
  RelationDefn _relationDefn;
  Object[] _values;

  //----------------------------------------------------------------------------
  // Constructor(s) (private, package, protected, public)
  //----------------------------------------------------------------------------

  /** Constructor... */
  public Tuple(Relation relation)
  {
    _relation = relation;
//    _relationDefn = relation.getRelationDefn();
  }

  //----------------------------------------------------------------------------
  // Public methods - accessors, mutators, other
  //----------------------------------------------------------------------------

  public Relation getRelation() { return _relation; }

  /** Return value of Tuple variable with <code>name</code> */
  public Object get(String name)
  {
      return null;
  }

  //----------------------------------------------------------------------------
  // Private methods
  //----------------------------------------------------------------------------

}
