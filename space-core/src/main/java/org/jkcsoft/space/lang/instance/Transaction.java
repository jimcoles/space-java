/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

/**
 * A Transaction is applicable to both in-memory and persistenct actions.  Every Action
 * implicitly defines a Transaction.  Transactions are therefore nestable.
 * <p>
 * With in-memory transactions, we might be able to avoid introducing the notion of Threads.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class Transaction {


    public Transaction() {
    }



}
