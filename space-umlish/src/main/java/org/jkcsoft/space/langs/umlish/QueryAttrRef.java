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

import java.util.List;

public class QueryAttrRef {
    //-------------------------------------------------------------------
    // Private instance vars
    //-------------------------------------------------------------------
    private Attribute _attr = null;
    private AssocChain _assocs = new AssocChain();

    //-------------------------------------------------------------------
    // Constructor(s)
    //-------------------------------------------------------------------
    QueryAttrRef(Association[] aAssocs, Attribute attr) {
        for (int idx = 0; idx < aAssocs.length; idx++) {
            _assocs.add(aAssocs[idx]);
        }
        _attr = attr;
    }

    public QueryAttrRef(List lAssocs, Attribute attr) {
        if (lAssocs != null) {
            _assocs = new AssocChain(lAssocs);
        }
        _attr = attr;
    }

    //-------------------------------------------------------------------
    // Public instance methods
    //-------------------------------------------------------------------
    public Attribute getAttr() {
        return _attr;
    }

    public AssocChain getAssocs() {
        return _assocs;
    }
  
/* 7/17/00 JKC Might need to override equals( ) if we let consuming objects
               create their own IQAttrRef's.  If we force them thru a factory
               that uses grabs from a common pool with no dupicates, etc. we 
               might not need this.
               
  public boolean equals(Object obj)
  {
    boolean retVal = false;
  	if (obj instanceof IQAttrRef) {
      retVal = true;
      List lasstest = ((IQAttrRef)obj).getAssocs();
      List lassthis = getAssocs();
      if (lasstest.size() != ) 
      {
        iass = lass.iterator();
        while (iass.hasNext()) {
          
        }
      }
      retVal = ( ((IXAssoc) obj).getIID().getLongValue() == getIID().getLongValue() );
    }
    return retVal;
  }
*/
}
