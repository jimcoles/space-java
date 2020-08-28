/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.test.core;

import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.ast.sji.SjiService;
import org.jkcsoft.space.lang.instance.Space;
import org.jkcsoft.space.lang.instance.TupleImpl;
import org.jkcsoft.space.lang.instance.TupleSet;
import org.jkcsoft.space.lang.instance.TupleSetImpl;
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
public class TestTupleSet extends TestSourceStub {

    Logger log = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Test
    public void apiTupleSet_SimpleType() {
        ApiExeContext spaceCtx = getNewApiExec();
        AstFactory ast = spaceCtx.getAstFactory();

        // 1. Build AST model via API
        TypeDefnImpl personType = ast.newTypeDefn("Person");
        VariableDecl userId = personType.addVariableDecl(ast.newVariableDecl("userId", NumPrimitiveTypeDefn.CARD));
        AssociationDefn firstName =
            personType.addAssociationDecl(ast.newAssociationDecl("firstName", personType, Executor.CHAR_SEQ_TYPE_DEF));
        AssociationDefn lastName =
            personType.addAssociationDecl(ast.newAssociationDecl("lastName", personType, Executor.CHAR_SEQ_TYPE_DEF));

        spaceCtx.getNsRegistry().getUserNs().getRootDir().addChild(personType);
        // declare keys
        personType.setPrimaryKey(ast.newKeyDefn(personType, ast.newProjectionDecl("UserPK", userId)));
        personType.addAlternateKey(ast.newKeyDefn(personType, ast.newProjectionDecl("NameAK", lastName, firstName)));
        //
        spaceCtx.attachTypesToUserNs(personType);

        // lets executor know you're done modifying the AST so it can do some figuring
        spaceCtx.apiAstLoadComplete();

        // 2. Create user objects via API
        TupleImpl tuple = spaceCtx.newTupleImpl(personType);
        // set by datum object
        tuple.setValue(userId, spaceCtx.getObjFactory().newCardinalValue(1L));
        // set by ordinal
        tuple.setValue(1, spaceCtx.newCharacterSequence("Jim"));
        tuple.setValue(2, spaceCtx.newCharacterSequence("Coles"));

        Space mySpace = spaceCtx.getDefaultSpace();
        mySpace.insert(tuple);

        TupleSetImpl tupleSet = spaceCtx.newSet(personType.getSetOfType());
        tupleSet.addTuple(tuple);

        log.info("API tuple set (int, string) => {}", spaceCtx.print(tupleSet));
    }

    /** Case: an API collection holds refs to SJI/Java tuples. */
    @Test
    public void apiType_ComplexType() {
        log.info("TODO");
    }

    /** Case: an API collection holds refs to SJI/Java tuples. */
    @Test
    public void apiTypeWithSjiAssocs() {
        log.info("TODO");
    }

    /** Case: Build Space set of tuples from Java collection. */
    @Test
    public void sjiTupleSet_SimpleType() {
        //
        ApiExeContext exec = getNewApiExec();
        SjiService sji = exec.getSjiService();
//        DatumType sjiGcProxy = sji.getSjiTypeProxyDeepLoad(GregorianCalendar.class, null);
        Calendar bdCal = GregorianCalendar.getInstance();
        //
        List<User> javaColl = List.of(
            new User("Mary", toMillis(bdCal, 1958, 1, 21)),
            new User("Di", toMillis(bdCal, 1959, 6, 16)),
            new User("Jim", toMillis(bdCal, 1964, 8, 28))
        );
        TupleSet tupleSet = sji.createSjiInstanceProxy(javaColl);
        log.info("SJI set (string, long) => {}", exec.print(tupleSet));
    }

    @Test
    public void sjiTupleSet_ComplexType() {
        ApiExeContext exec = getNewApiExec();
        SjiService sji = exec.getSjiService();
        // Use Calendar object
        // TODO: how to wrap a Java GregorianCalendar as a Space Tuple w/ Value
        TypeDefn sjiUserC = sji.getSjiTypeProxyDeepLoad(UserC.class);
        TypeDefn sjiGC = sji.getSjiTypeProxyDeepLoad(GregorianCalendar.class);
        AssociationDefn userBday = (AssociationDefn) sjiUserC.getDatum("birthDate");
//            userBday.setAssociationKind(AssociationKind.DEPENDENT);

        List<UserC> javaCollC = List.of(
            new UserC("Mary", newCal(1958, 0, 21)),
            new UserC("Di", newCal(1959, 5, 16)),
            new UserC("Jim", newCal(1964, 7, 28))
        );
        TupleSet tupleSetC = sji.createSjiInstanceProxy(javaCollC);
        //
        log.info("SJI tuples (string, Calendar) => {}", exec.print(tupleSetC));
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
