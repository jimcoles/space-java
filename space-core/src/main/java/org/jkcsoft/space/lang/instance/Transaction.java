/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

/**
 * A Transaction is applicable to both in-memory and persistence actions. Every Action
 * implicitly defines a Transaction. Transactions are therefore nestable.
 *
 * <p>In-memory Transactions might offset the need for direct thread control by the
 * programmer.
 *
 * <p>Aka, 'object delta',
 *
 * @author Jim Coles
 * @version 1.0
 */
public class Transaction {

    enum Kind {
        CREATE, // the system creates one or mre identities
        UPDATE  // the identity is provided by external system
    }

    private Kind kind;
    private Tuple values;

    public Transaction() {

    }

}
