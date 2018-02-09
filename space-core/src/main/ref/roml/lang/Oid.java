/*
 * Copyright 2002 Systematic Solutions Corporation, All Rights Reserved.
 *
 * This software is the proprietary information of Systematic Solutions Corporation.
 * Use is subject to license terms.
 *
*/
package com.jkc.roml.lang;

/**
 * Encapsulates an object identifier within the Systematic data management
 * system.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class Oid extends Identifier
{
  private long _oid;

  public Oid()
  {
  }

  public long getOid() { return _oid; }

}