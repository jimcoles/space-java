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

    public static DatumTupleComparator buildDatumComparator(Declaration datumDecl) {
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
            List<ProjectionDecl> keyVars = key.getProjectionDeclList();
            for (ProjectionDecl keyVar : keyVars) {
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

        private Declaration datumDecl;

        public DatumTupleComparator(Declaration datumDecl) {
            this.datumDecl = datumDecl;
        }

        @Override
        public int compare(Tuple tuple1, Tuple tuple2) {
            int retVal;
            ScalarValue v1;
            ScalarValue v2;
            if (datumDecl.isAssoc()) {
                // TODO: sort by key of the target tuple
                throw new IllegalArgumentException("we don't yet handle assoc comparisons. var[" + datumDecl + "]");
            }
            else {
                v1 = (ScalarValue) tuple1.get(datumDecl).getValue();
                v2 = (ScalarValue) tuple2.get(datumDecl).getValue();
                retVal = datumDecl.getType().getValueComparator().compare(v1, v2);
            }
            return retVal;
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

    }

}
