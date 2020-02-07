/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast.sji;

import org.apache.commons.lang.ArrayUtils;
import org.jkcsoft.space.lang.instance.*;
import org.jkcsoft.space.lang.instance.sji.SjiFieldValueHolder;
import org.jkcsoft.space.lang.instance.sji.SjiParamValueHolder;
import org.jkcsoft.space.lang.instance.sji.SjiPropValueHolder;
import org.jkcsoft.space.lang.instance.sji.SjiTuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jkcsoft.java.util.Beans;
import org.jkcsoft.space.SpaceHome;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.runtime.AstUtils;
import org.jkcsoft.space.lang.runtime.Executor;
import org.jkcsoft.space.lang.runtime.SpaceX;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.*;

import static org.jkcsoft.space.SpaceHome.getAstFactory;

/**
 * @author Jim Coles
 */
public class SjiService {

    private static final Logger log = LoggerFactory.getLogger(SjiService.class);

    private NSRegistry nsRegistry = SpaceHome.getNsRegistry();
    private SjiBindings sjiBindings = new SjiBindings();
    private Map<Class, SjiTypeMapping> sjiMappingByClass = new HashMap<>();
    private Map<String, SjiTypeMapping> sjiMappingByName = new HashMap<>();
    private ObjectFactory spaceObjFactory;

    public SjiService() {
        spaceObjFactory = ObjectFactory.getInstance();
    }

    public SjiBindings getSjiBindings() {
        return sjiBindings;
    }

    /**
     * Loads Space wrapper of Java class into the Java NS.
     *
     * Called after the list of needed Java
     * classes has been extracted from Space source, but prior to exec-time.
     * At exec-time, wrapper types will be looked up via the same Namespace lookup
     * methods used to find pure Space types.
     *
     * @param className Standard Java fully-qualified class name "java.lang.String".
     */
    public DatumType getDeepLoadSpaceWrapper(String className, String[] overrideDirNames)
        throws ClassNotFoundException
    {
        DatumType wrapper = null;
        SjiTypeMapping sjiTypeMapping = sjiMappingByName.get(className);
        if (sjiTypeMapping == null) {
            Class<?> jnClass = Class.forName(className);
            wrapper = getDeepLoadSpaceWrapper(jnClass, overrideDirNames);
        }
        else {
            wrapper = sjiTypeMapping.getSpaceWrapper();
        }
        return wrapper;
    }

    public DatumType getDeepLoadSpaceWrapper(Class<?> jnClass, String[] overrideDirNames) {
        SjiTypeMapping sjiTypeMapping = sjiMappingByClass.get(jnClass);
        if (sjiTypeMapping == null) {
            sjiTypeMapping = createSjiTypeMapping(jnClass);
            deepLoadSpaceWrapper(sjiTypeMapping, overrideDirNames);
        }
        else {
            if (sjiTypeMapping.getState() != LinkState.RESOLVED) {
                throw new SpaceX("space wrapper type is not loaded for ["+jnClass+"]");
            }
        }
        return sjiTypeMapping.getSpaceWrapper();
    }

    private void deepLoadSpaceWrapper(SjiTypeMapping sjiTypeMapping, String[] overrideDirNames) {
        String[] packageNameParts;
        if (overrideDirNames != null)
            packageNameParts = overrideDirNames;
        else {
            String spaceTypeName = getSjiBindings().applyToJavaClassname(sjiTypeMapping.getJavaClass().getName());
            String[] spaceTypeNameParts = splitClassName(spaceTypeName);
            packageNameParts = new String[spaceTypeNameParts.length - 1];
            System.arraycopy(spaceTypeNameParts, 0, packageNameParts, 0, packageNameParts.length);
        }

        Directory parentDir =
            AstUtils.ensureDir(nsRegistry.getJavaNs().getRootDir(), packageNameParts);

        //
        // Build wrappers for this class and any required classes
        //
        ParseUnit newParseUnit = buildShallowSjiType(sjiTypeMapping.getJavaClass());
        sjiTypeMapping.setSpaceWrapper(newParseUnit.getTypeDefns().get(0));
        sjiTypeMapping.setState(LinkState.RESOLVED);
        //
        parentDir.addParseUnit(newParseUnit);

//        nsRegistry.trackMetaObject(sjiTypeDefn);

        // load dependencies ...
        Set<SjiTypeRefByClass> unresolvedRefs = AstUtils.queryAst(newParseUnit,
                                                                  new Executor.QueryAstConsumer<>(
                                                                   SjiTypeRefByClass.class,
                                                                   modelElement ->
                                                                       modelElement instanceof SjiTypeRefByClass &&
                                                                           ((SjiTypeRefByClass) modelElement).getState() !=
                                                                               LinkState.RESOLVED)
        );
        for (SjiTypeRefByClass unresolvedRef : unresolvedRefs) {
            if (unresolvedRef.getState() == LinkState.INITIALIZED) {
                deepLoadSpaceWrapper(unresolvedRef.getMapping(), null);
//                DatumType spaceWrapper = getDeepLoadSpaceWrapper(unresolvedRef.getWrappedClass());
                if (unresolvedRef.getState() != LinkState.RESOLVED) {
                    log.error("space wrapper for Java class " +
                                  "[" + unresolvedRef.getWrappedClass().getName() + "]" +
                                  " did not load properly");
                }
            }
            else {
                log.info("skipping load of previously unresolved ref");
            }
        }
        log.debug("created Space wrapper mapping: " + sjiTypeMapping);
    }

    public PrimitiveTypeDefn getPrimitiveTypeDefn(Class jnClass) {
        SjiTypeMapping sjiTypeMapping = getOrCreateSjiMapping(jnClass);
        if (sjiTypeMapping == null)
            throw new IllegalStateException("A Space type could not be created for Java type ["+jnClass+"]");
        if (!sjiTypeMapping.isPrimitive())
            throw new IllegalArgumentException("Java class ["+jnClass+"] does not map to a Space primitive type");
        return (PrimitiveTypeDefn) sjiTypeMapping.getSpaceWrapper();
    }

    SjiTypeMapping getOrCreateSjiMapping(Class jnClass) {
        SjiTypeMapping sjiTypeMapping = sjiMappingByClass.get(jnClass);
        if (sjiTypeMapping == null) {
            sjiTypeMapping = createSjiTypeMapping(jnClass);
        }
        return sjiTypeMapping;
    }

    private SjiTypeMapping createSjiTypeMapping(Class jnClass) {
        SjiTypeMapping sjiTypeMapping = new SjiTypeMapping(jnClass);
        sjiMappingByClass.put(jnClass, sjiTypeMapping);
        sjiMappingByName.put(jnClass.getName(), sjiTypeMapping);
        return sjiTypeMapping;
    }

    private ParseUnit buildShallowSjiType(Class jnClass) {
        SjiTypeDefn sjiTypeDefn = newNativeTypeDefn(jnClass);
        //
        ParseUnit parseUnit = getAstFactory().newParseUnit(new NativeSourceInfo(jnClass));
        parseUnit.addTypeDefn(sjiTypeDefn);

        // Add datums
        PropertyDescriptor[] jPropDescriptors = Beans.getPropertyDescriptors(jnClass);
        for (PropertyDescriptor jPropDescriptor : jPropDescriptors) {
            if (!isExcluded(jPropDescriptor))
                sjiTypeDefn.addVariableDecl(new SjiPropVarDecl(sjiTypeDefn, jPropDescriptor));
        }
        Field[] jFields = jnClass.getDeclaredFields();
        for (Field jField : jFields) {
            if (!isExcluded(jField))
                sjiTypeDefn.addVariableDecl(new SjiFieldVarDecl(sjiTypeDefn, jField));
        }

        // Add functions
        Method[] methods = jnClass.getMethods();
        for (Method jMethod : methods) {
            if (isExcluded(jMethod))
                continue;

            SjiTypeRefByClass retTypeRef = new SjiTypeRefByClass(jMethod, getOrCreateSjiMapping(jMethod.getReturnType()));
            // build arg type defn
            SjiTypeDefn argTupleTypeDefn = newNativeTypeDefn(null);
            Parameter[] jParameters = jMethod.getParameters();
            for (Parameter jParam : jParameters) {
                SjiTypeMapping sjiParamTypeMapping = getOrCreateSjiMapping(jParam.getType());
                SjiTypeRefByClass paramTypeRef = new SjiTypeRefByClass(jParam, sjiParamTypeMapping);
                if (!sjiParamTypeMapping.isPrimitive()) {
                    argTupleTypeDefn.addAssociationDecl(
//                        newAssociationDecl(jParam, sjiParamTypeInfo)
                        getAstFactory()
                            .newAssociationDecl(paramTypeRef.getSourceInfo(), jParam.getName(), paramTypeRef)
                    );
                }
                else {
                    argTupleTypeDefn.addVariableDecl(newVariableDecl(jParam, argTupleTypeDefn));
                }
            }

            SjiFunctionDefnImpl sjiFunctionDefnImpl = newNativeFunctionDefn(
                sjiTypeDefn,
                jMethod.getName(),
                jMethod,
                argTupleTypeDefn,
                retTypeRef
            );
            //
            sjiTypeDefn.addFunctionDefn(sjiFunctionDefnImpl);
            nsRegistry.trackMetaObject(sjiFunctionDefnImpl);
            //
        }
        return parseUnit;
    }

    private SjiVarDecl newVariableDecl(Parameter jParam, SjiTypeDefn sjiTypeDefn) {
        return new SjiParamVarDecl(jParam, sjiTypeDefn);
    }


//    private SjiAssocDecl newAssociationDecl(Parameter jParam, SjiTypeMapping sjiTypeMapping) {
    public SjiTypeDefn newNativeTypeDefn(Class jClass) {
        return new SjiTypeDefn(jClass);
    }

    //    }
    public SjiFunctionDefnImpl newNativeFunctionDefn(SjiTypeDefn parentTypeDefn, String name, Method jMethod,
                                                     SjiTypeDefn argTypeDefn, SjiTypeRefByClass returnTypeRef)
    {
        SjiFunctionDefnImpl element = new SjiFunctionDefnImpl(parentTypeDefn, jMethod, name, returnTypeRef);
        element.setArgSpaceTypeDefn(argTypeDefn);
        return element;
    }

    /**
     * This method wraps the Java collection and objects with SJI wrapper classes such that
     * reads, writes, and function calls on the wrappers operate on the underlying Java objects.
     * @return
     */
    public SetSpace createSjiProxy(Collection<?> javaColl) {
        if (javaColl == null) {
            return null;
        }

        Class<?> containedJavaClass = javaColl.iterator().next().getClass();
        SjiTypeDefn sjiContainedTypeDefn = (SjiTypeDefn) getDeepLoadSpaceWrapper(containedJavaClass, null);
        SetSpace setSpace = spaceObjFactory.newSet(null, sjiContainedTypeDefn.getSetOfType());

        for (Object javaObj : javaColl) {
            SjiTuple tuple = createSjiProxy(sjiContainedTypeDefn, javaObj);
            setSpace.addTuple(tuple);
        }
        return setSpace;
    }

    private SjiTuple createSjiProxy(SjiTypeDefn sjiTypeDefn, Object javaObj) {
        SjiTuple sjiTuple = emptySjiTuple(sjiTypeDefn, javaObj);
        List<Declaration> datumDeclList = sjiTypeDefn.getDatumDeclList();
        for (Declaration spaceDatumDecl : datumDeclList) {
            sjiTuple.initHolder(createSjiProxy(sjiTuple, spaceDatumDecl));
        }
        return sjiTuple;
    }

    /** Tuple values are uninitialized. */
    private SjiTuple emptySjiTuple(SjiTypeDefn sjiTypeDefn, Object javaObj) {
        return new SjiTuple(spaceObjFactory.newOid(), sjiTypeDefn, javaObj);
    }

    private void initSjiTuple(SjiTypeDefn defn, SjiTuple tuple) {
        // initialize
        if (defn.hasDatums()) {
            List<Declaration> declList = defn.getDatumDeclList();
            for (Declaration datumDecl : declList) {
                tuple.initHolder(createSjiProxy(tuple, datumDecl));
            }
        }

//        spaceObjFactory.trackInstanceObject((SpaceObject) tuple);
    }

    private ValueHolder createSjiProxy(SjiTuple tuple, Declaration datumDecl) {
        ValueHolder holder = null;
        if (datumDecl instanceof SjiPropVarDecl) {
            holder = new SjiPropValueHolder(tuple, ((SjiPropVarDecl) datumDecl));
        }
        else if (datumDecl instanceof SjiFieldVarDecl) {
            holder = new SjiFieldValueHolder(tuple, ((SjiFieldVarDecl) datumDecl));
        }
        else if (datumDecl instanceof SjiParamVarDecl) {
            holder = new SjiParamValueHolder(tuple, ((SjiParamVarDecl) datumDecl));
        }
        return holder;
    }

    static FullTypeRefImpl.CollectionType javaToSpaceCollType(Class clazz) {
        FullTypeRefImpl.CollectionType spCollType = null;
        if (clazz == String.class) {
            spCollType = FullTypeRefImpl.CollectionType.SEQUENCE;
        }
        else {
            spCollType = clazz.isArray() ? FullTypeRefImpl.CollectionType.SEQUENCE : null;
        }
        return spCollType;
    }

    private String[] splitClassName(String className) {
        return className.split("\\.");
    }

    private boolean isExcluded(Method jMethod) {
        boolean exclude =
            !isPublicMethod(jMethod)
                || ArrayUtils.contains(Object.class.getMethods(), jMethod);
        return exclude;
    }

    private boolean isExcluded(Field jField) {
        boolean exclude =
            !Modifier.isPublic(jField.getModifiers())
                || ArrayUtils.contains(Object.class.getFields(), jField);
        return exclude;
    }

    private boolean isExcluded(PropertyDescriptor jPropDescriptor) {
        // exclude if prop is not readable and not writable
        boolean exclude =
            !isPublicMethod(jPropDescriptor.getReadMethod())
                || !isPublicMethod(jPropDescriptor.getWriteMethod());
        return exclude;
    }

    private boolean isPublicMethod(Method jMethod) {
        return jMethod != null && Modifier.isPublic(jMethod.getModifiers());
    }

    static String toSpaceTypeFQName(Class sjiClass) {
        return sjiClass.getName();
    }
}
