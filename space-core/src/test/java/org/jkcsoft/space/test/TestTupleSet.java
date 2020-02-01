/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.test;

import org.jkcsoft.space.SpaceHome;
import org.jkcsoft.space.lang.ast.sji.SjiService;
import org.jkcsoft.space.lang.ast.sji.SjiTypeDefn;
import org.jkcsoft.space.lang.instance.ObjectFactory;
import org.jkcsoft.space.lang.instance.SetSpace;
import org.junit.Test;

import java.util.*;

/**
 * @author Jim Coles
 */
public class TestTupleSet {
    /**
     * Build Space set of tuples from Java collection.
     */
    @Test public void testTupleSetBuild() {
//        List
        SjiService sji = SpaceHome.getSjiService();
        SjiTypeDefn sjiTypeDefn = (SjiTypeDefn) sji.getDeepLoadSpaceWrapper(User.class, null);
//        SpaceHome.getAstFactory().newT
        ObjectFactory objFactory = ObjectFactory.getInstance();
        SetSpace setSpace = objFactory.newSet(null, sjiTypeDefn.getSetOfType());
        Calendar bdCal = GregorianCalendar.getInstance();
        List<User> javaColl = List.of(
            new User("Jim", toMillis(bdCal, 1964, 8, 28)),
            new User("Mary", toMillis(bdCal, 1958, 1, 21)),
            new User("Di", toMillis(bdCal, 1959, 6, 16))
        );
        sji.javaToSpace(javaColl, setSpace);
        //

    }

    private long toMillis(Calendar bdCal, int year, int month, int date) {
        bdCal.set(year, month, date);
        return bdCal.getTimeInMillis();
    }

    public static class User {
        private String name;
        private long birthDate;

        public User(String name, long birthDate) {
            this.name = name;
            this.birthDate = birthDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(long birthDate) {
            this.birthDate = birthDate;
        }
    }
}
