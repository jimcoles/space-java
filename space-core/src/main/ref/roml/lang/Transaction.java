/*
 * Copyright 2003 Systematic Solutions Corporation, All Rights Reserved.
 *
 * This software is the proprietary information of Systematic Solutions Corporation.
 * Use is subject to license terms.
 *
 */
package com.jkc.roml.lang;

/**
 * A Transaction is applicable to both in-memory and persistenct actions.  Every Action
 * implicitly defines a Transaction.  Transactions are therefore nestable.
 *
 * With in-memory transactions, we might be able to avoid introducing the notion of Threads.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class Transaction
{
  //----------------------------------------------------------------------------
  // Private instance vars
  //----------------------------------------------------------------------------

  //----------------------------------------------------------------------------
  // Constructor(s) (private, package, protected, public)
  //----------------------------------------------------------------------------

  /** Constructor... */
  public Transaction()
  {
  }

  //----------------------------------------------------------------------------
  // Public methods - accessors, mutators, other
  //----------------------------------------------------------------------------

  //----------------------------------------------------------------------------
  // Private methods
  //----------------------------------------------------------------------------

}
