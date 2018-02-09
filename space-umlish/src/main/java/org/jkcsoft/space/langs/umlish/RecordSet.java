/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
*/
package org.jkcsoft.space.langmaps.umlish;


/**
 * Basic collection for holding a set of data or tuples of a relation.  Analogous to an XML Document.
 * <p>
 * Better name might be TupleSet.
 * <p>
 * Like a JDBC RecordSet or RowSet except:
 * - Is inherently detached from the database.
 * -
 * <p>
 * ISSUE: Is record set the proper notion.  In general these contents will contain
 * a collection of object graphs with incomplete attribute lists.
 * <p>
 * Features:
 * Consider first class notions for Iterator and Cursor (Pager).
 */
public class RecordSet {

    public RecordSet() {
    }

}