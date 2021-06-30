/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import org.jkcsoft.space.lang.instance.*;

import java.util.Comparator;
import java.util.List;

/**
 * @author Jim Coles
 */
public class Comparators {

    private static final Comparator<RealValue>  REAL_COMP_ASC   = Comparator.comparingDouble(ScalarValue::getJavaValue);
    private static final Comparator<String>     STRING_COMP_ASC = String::compareTo;

    public static ProjectionComparator buildProjectionComparator(KeyDefn keyDefn) {
        return new ProjectionComparator(keyDefn);
    }

    public static DatumTupleComparator buildDatumComparator(DatumDecl datumDecl) {
        return new DatumTupleComparator(datumDecl);
    }

    /**
     * Compares two Tuples of the same type given a full key sequence.
     */
    public static class ProjectionComparator implements Comparator<Tuple> {

        private final KeyDefn key;

        public ProjectionComparator(KeyDefn key) {
            this.key = key;
        }

        @Override
        public int compare(Tuple o1, Tuple o2) {
            int comp = 0;
            List<DatumProjectionExpr> keyVars = key.getProjectionDeclList();
            for (DatumProjectionExpr keyVar : keyVars) {
                comp = keyVar.getDatumComparator().compare(o1, o2);
                if (comp != 0)
                    break;
            }
            return comp;
        }
    }

    /**
     * Compares a given named variable (possibly a projection) value of two tuples.
     * @author Jim Coles
     */
    public static class DatumTupleComparator implements Comparator<Tuple> {

        private DatumDecl datumDecl;

        public DatumTupleComparator(DatumDecl datumDecl) {
            this.datumDecl = datumDecl;
        }

        @Override
        public int compare(Tuple tuple1, Tuple tuple2) {
            int retVal;
            if (datumDecl.hasAssoc()) {
                // TODO: sort by key of the target tuple
                throw new IllegalArgumentException("we don't yet handle assoc comparisons. var[" + datumDecl + "]");
            }
            else {
                retVal = datumDecl.getType().getValueComparator().compare(
                    (ScalarValue) tuple1.get(datumDecl).getValue(),
                    (ScalarValue) tuple2.get(datumDecl).getValue()
                );
            }
            return retVal;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

    }

}
