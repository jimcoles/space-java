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
import org.apache.log4j.Logger;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.jkcsoft.space.SpaceHome.getAstFactory;

/**
 * @author Jim Coles
 */
public class SjiService {

    private static final Logger log = Logger.getLogger(SjiService.class);

    private NSRegistry nsRegistry = SpaceHome.getNsRegistry();

    private Map<Class, SjiTypeMapping> sjiMappingByClass = new HashMap<>();
    private Map<String, SjiTypeMapping> sjiMappingByName = new HashMap<>();

    public SjiService() {

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
        String[] classNameParts = splitClassName(sjiTypeMapping.getJavaClass().getName());
        String[] packageNameParts;
        if (overrideDirNames != null)
            packageNameParts = overrideDirNames;
        else {
            packageNameParts = new String[classNameParts.length - 1];
            System.arraycopy(classNameParts, 0, packageNameParts, 0, packageNameParts.length);
        }

        Directory parentDir =
            AstUtils.ensureDir(nsRegistry.getJavaNs().getRootDir(), packageNameParts);

        //
        // Build wrappers for this class and any required classes
        //
        ParseUnit newParseUnit = buildShallowNativeType(sjiTypeMapping.getJavaClass());
        sjiTypeMapping.setSpaceWrapper(newParseUnit.getTypeDefns().get(0));
        sjiTypeMapping.setState(LinkState.RESOLVED);
        //
        parentDir.addParseUnit(newParseUnit);

//        nsRegistry.trackMetaObject(sjiTypeDefn);

        // load dependencies ...
        Set<TypeRefByClass> unresolvedRefs = AstUtils.queryAst(newParseUnit,
                                                               new Executor.QueryAstConsumer<>(
                                                                   TypeRefByClass.class,
                                                                   modelElement ->
                                                                       modelElement instanceof TypeRefByClass &&
                                                                           ((TypeRefByClass) modelElement).getState() !=
                                                                               LinkState.RESOLVED)
        );
        for (TypeRefByClass unresolvedRef : unresolvedRefs) {
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

    private ParseUnit buildShallowNativeType(Class jnClass) {
        SjiTypeDefn sjiTypeDefn = newNativeTypeDefn(jnClass);
        //
        ParseUnit parseUnit = getAstFactory().newParseUnit(new NativeSourceInfo(jnClass));
        parseUnit.addTypeDefn(sjiTypeDefn);

        // Add datums
        PropertyDescriptor[] jPropDescriptors = Beans.getPropertyDescriptors(jnClass);
        for (PropertyDescriptor jPropDescriptor : jPropDescriptors) {
            if (isExcludeNative(jPropDescriptor))
                sjiTypeDefn.addVariableDecl(new SjiPropVarDecl(sjiTypeDefn, jPropDescriptor));
        }
        Field[] jFields = jnClass.getDeclaredFields();
        for (Field jField : jFields) {
            if (isExcludeNative(jField))
            sjiTypeDefn.addVariableDecl(new SjiFieldVarDecl(sjiTypeDefn, jField));
        }

        // Add functions
        Method[] methods = jnClass.getMethods();
        for (Method jMethod : methods) {
            if (isExcludedNative(jMethod))
                continue;

            TypeRefByClass retTypeRef = new TypeRefByClass(jMethod, getOrCreateSjiMapping(jMethod.getReturnType()));
            // build arg type defn
            SjiTypeDefn argTupleTypeDefn = newNativeTypeDefn(null);
            Parameter[] jParameters = jMethod.getParameters();
            for (Parameter jParam : jParameters) {
                SjiTypeMapping sjiParamTypeMapping = getOrCreateSjiMapping(jParam.getType());
                TypeRefByClass paramTypeRef = new TypeRefByClass(jParam, sjiParamTypeMapping);
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
                                                     SjiTypeDefn argTypeDefn, TypeRefByClass returnTypeRef)
    {
        SjiFunctionDefnImpl element = new SjiFunctionDefnImpl(parentTypeDefn, jMethod, name, returnTypeRef);
        element.setArgSpaceTypeDefn(argTypeDefn);
        return element;
    }

    //        return null;
    static TypeRefImpl.CollectionType javaToSpaceCollType(Class clazz) {
        TypeRefImpl.CollectionType spCollType = null;
        if (clazz == String.class) {
            spCollType = TypeRefImpl.CollectionType.SEQUENCE;
        }
        else {
            spCollType = clazz.isArray() ? TypeRefImpl.CollectionType.SEQUENCE : null;
        }
        return spCollType;
    }

    private String[] splitClassName(String className) {
        return className.split("\\.");
    }

    private boolean isExcludedNative(Method jMethod) {
        boolean exclude = !isPublicMethod(jMethod);
        exclude = exclude || ArrayUtils.contains(Object.class.getMethods(), jMethod);
        return exclude;
    }

    private boolean isExcludeNative(Field jField) {
        boolean exclude = !Modifier.isPublic(jField.getModifiers()) ;
        exclude = exclude || ArrayUtils.contains(Object.class.getFields(), jField);
        return exclude;
    }

    private boolean isExcludeNative(PropertyDescriptor jPropDescriptor) {
        // exclude if prop is not readable and not writable
        boolean exclude =
            !(isPublicMethod(jPropDescriptor.getReadMethod())
                || isPublicMethod(jPropDescriptor.getWriteMethod())
            );
        return exclude;
    }

    private boolean isPublicMethod(Method jMethod) {
        return jMethod != null && Modifier.isPublic(jMethod.getModifiers());
    }

    static String toSpaceTypeFQName(Class sjiClass) {
        return sjiClass.getName();
    }

}
