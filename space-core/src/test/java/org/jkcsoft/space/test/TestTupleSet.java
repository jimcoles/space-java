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

import org.jkcsoft.space.lang.ast.AstFactory;
import org.jkcsoft.space.lang.ast.NumPrimitiveTypeDefn;
import org.jkcsoft.space.lang.ast.TypeDefnImpl;
import org.jkcsoft.space.lang.ast.sji.SjiService;
import org.jkcsoft.space.lang.instance.TupleImpl;
import org.jkcsoft.space.lang.instance.TupleSet;
import org.jkcsoft.space.lang.runtime.ApiExeContext;
import org.jkcsoft.space.lang.runtime.Executor;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Jim Coles
 */
public class TestTupleSet {

    Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Test
    public void testTupleSetApi() {
        ApiExeContext exec = Executor.defaultInstance();
        AstFactory ast = exec.getAstFactory();
        TypeDefnImpl personType =
            ast.newTypeDefn("Person");
        personType.addAssociationDecl(ast.newAssociationDecl("firstName", Executor.CHAR_SEQ_TYPE_DEF));
        personType.addVariableDecl(ast.newVariableDecl("userId", NumPrimitiveTypeDefn.CARD));
        //
        TupleImpl tuple = exec.newTupleImpl(personType);
        tuple.setValue(0, exec.getObjFactory().newCardinalValue(1L));
        tuple.setValue(1, exec.newCharacterSequence("Jim"));
    }

    /**
     * Build Space set of tuples from Java collection.
     */
    @Test
    public void testJavaTupleSetBuild() {
//        List
        ApiExeContext exec = Executor.defaultInstance();
        SjiService sji = exec.getSjiService();
//        DatumType sjiGcProxy = sji.getSjiTypeProxyDeepLoad(GregorianCalendar.class, null);
        Calendar bdCal = GregorianCalendar.getInstance();
        //
        {
            List<User> javaColl = List.of(
                new User("Mary", toMillis(bdCal, 1958, 1, 21)),
                new User("Di", toMillis(bdCal, 1959, 6, 16)),
                new User("Jim", toMillis(bdCal, 1964, 8, 28))
            );
            TupleSet tupleSet = sji.createSjiInstanceProxy(javaColl);
            log.info("SJI set => {}", exec.print(tupleSet));
        }
        // Use Calendar object
        // TODO: how to wrap a Java GregorianCalendar as a Space Tuple w/ Value
        {
            List<UserC> javaCollC = List.of(
                new UserC("Mary", newCal(1958, 1, 21)),
                new UserC("Di", newCal(1959, 6, 16)),
                new UserC("Jim", newCal(1964, 8, 28))
            );
            TupleSet tupleSetC = sji.createSjiInstanceProxy(javaCollC);
            //
            log.info("SJI set => {}", exec.print(tupleSetC));
        }
    }

    private long toMillis(Calendar bdCal, int year, int month, int date) {
        bdCal.set(year, month, date);
        return bdCal.getTimeInMillis();
    }

    private Calendar newCal(int year, int month, int date) {
        Calendar bdCal = GregorianCalendar.getInstance();
        bdCal.set(year, month, date);
        return bdCal;
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

    public static class UserC {
        private String name;
        private Calendar birthDate;

        public UserC(String name, Calendar birthDate) {
            this.name = name;
            this.birthDate = birthDate;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Calendar getBirthDate() {
            return birthDate;
        }

        public void setBirthDate(Calendar birthDate) {
            this.birthDate = birthDate;
        }
    }

}
