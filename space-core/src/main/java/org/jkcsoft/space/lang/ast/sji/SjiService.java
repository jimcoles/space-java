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
import org.jkcsoft.java.util.Beans;
import org.jkcsoft.space.SpaceHome;
import org.jkcsoft.space.lang.ast.*;
import org.jkcsoft.space.lang.loader.AstErrors;
import org.jkcsoft.space.lang.loader.AstLoader;

import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;

import static org.jkcsoft.space.SpaceHome.getAstFactory;

/**
 * @author Jim Coles
 */
public class SjiService implements AstLoader {

    private NSRegistry nsRegistry = SpaceHome.getNsRegistry();

    private Map<Class, SjiTypeMapping> sjiMappings = new TreeMap<>();

    public SjiService() {
    }

    @Override
    public String getName() {
        return "SjiLoader";
    }

    @Override
    public ParseUnit loadFile(AstErrors parentErrors, Directory spaceDir, File spaceSrcFile) throws IOException {
        return null;
    }

    @Override
    public Directory loadFromResource(String path) {
        return null;
    }

    @Override
    public Directory loadDir(AstErrors parentErrors, File srcDir) throws IOException {
        return null;
    }

    public DatumType getNativeType(Class jnClass) {
        DatumType spcDatumType = null;
        SjiTypeMapping sjiTypeMapping = getSjiMapping(jnClass);
        if (sjiTypeMapping == null)
            throw new IllegalStateException("A Space type could not be created for Java type ["+jnClass+"]");
        return spcDatumType;
    }

    public SjiTypeMapping getSjiMapping(Class jnClass) {
        SjiTypeMapping sjiTypeMapping = sjiMappings.get(jnClass);
        if (sjiTypeMapping == null) {
            sjiTypeMapping = buildNativeType(jnClass);
            sjiMappings.put(jnClass, sjiTypeMapping);
        }
        return sjiTypeMapping;
    }

    private SjiTypeMapping buildNativeType(Class jnClass) {
        SjiTypeMapping sjiTypeMapping = new SjiTypeMapping(jnClass);
        SjiTypeDefn sjiTypeDefn = newNativeTypeDefn(jnClass);

        nsRegistry.trackMetaObject(sjiTypeDefn);
        //
        nsRegistry.getJavaNs().getRootDir().addSpaceDefn(sjiTypeDefn);

        // Add datums
        PropertyDescriptor[] jPropDescriptors = Beans.getPropertyDescriptors(jnClass);
        for (PropertyDescriptor jPropDescriptor : jPropDescriptors) {
            jPropDescriptor.getReadMethod();
        }

        // Add functions
        Method[] methods = jnClass.getMethods();
        for (Method jMethod : methods) {
            if (isExcludedNative(jMethod))
                continue;

            Class<?> jType = jMethod.getReturnType();
            SjiTypeMapping spiRetTypeInfo = getSjiMapping(jType);

            // build arg type defn
            SjiTypeDefn argTypeDefn = newNativeTypeDefn(null);
            Parameter[] jParameters = jMethod.getParameters();
            for (Parameter jParam : jParameters) {
                SjiTypeMapping sjiMapping = getSjiMapping(jParam.getType());
                if (!sjiMapping.isSpacePrim()) {
                    argTypeDefn.addAssociationDecl(newAssociationDecl(jParam, sjiMapping));
                }
                else {
                    argTypeDefn.addVariableDecl(newVariableDecl(jParam, sjiMapping.getSpacePrimType()));
                }
            }

            SjiFunctionDefnImpl sjiFunctionDefnImpl = newNativeFunctionDefn(
                sjiTypeDefn,
                jMethod.getName(),
                jMethod,
                argTypeDefn,
                spiRetTypeInfo.getSpaceTypeRef()
            );
            //
            sjiTypeDefn.addFunctionDefn(sjiFunctionDefnImpl);
            nsRegistry.trackMetaObject(sjiFunctionDefnImpl);
            //
        }
        return sjiTypeMapping;
    }

    private SjiVarDecl newVariableDecl(Parameter jParam, PrimitiveTypeDefn spacePrimType) {
        return null;
    }

    private SjiAssocDecl newAssociationDecl(Parameter jParam, SjiTypeMapping sjiTypeMapping) {
        return null;
    }

    private SjiVarDecl newNativeVarDecl(Class<?> jParamType, String s) {
        return null;
    }

    public SjiTypeDefn newNativeTypeDefn(Class jClass) {
        return new SjiTypeDefn(jClass);
    }

    public SjiFunctionDefnImpl newNativeFunctionDefn(SjiTypeDefn parentTypeDefn, String name, Method jMethod,
                                                     SjiTypeDefn argTypeDefn, TypeRef returnTypeRef)
    {
        SjiFunctionDefnImpl element = new SjiFunctionDefnImpl(parentTypeDefn, jMethod, name, returnTypeRef);
        element.setArgSpaceTypeDefn(argTypeDefn);
        return element;
    }

    private static TypeRef.CollectionType javaToSpaceCollType(Class clazz) {
        TypeRef.CollectionType spCollType = null;
        if (clazz == String.class) {
            spCollType = TypeRef.CollectionType.SEQUENCE;
        }
        else {
            spCollType = clazz.isArray() ? TypeRef.CollectionType.SEQUENCE : null;
        }
        return spCollType;
    }

    private boolean isExcludedNative(Method jMethod) {
        Method[] baseMethods = Object.class.getMethods();
        return ArrayUtils.contains(baseMethods, jMethod);
    }

    private static String toSpaceTypeName(Class sjiClass) {
//        return spaceJavaWrapperClass.getSimpleName().substring(2);
        return sjiClass.getSimpleName();
    }

    public void loadJarDir(JarEntry jarEntry) {

    }

    public static class SjiTypeMapping {
        private boolean isSpacePrim = false;
        private TypeRef spaceTypeRef;
        private PrimitiveTypeDefn spcPrimType;
        private SourceInfo jSourceInfo;

        public SjiTypeMapping(Class jClass) {
            this.jSourceInfo = new NativeSourceInfo(jClass);
            spcPrimType = null;
            if (jClass == Boolean.TYPE) {
                spcPrimType = NumPrimitiveTypeDefn.BOOLEAN;
            }
            else if (jClass == Integer.TYPE) {
                spcPrimType = NumPrimitiveTypeDefn.CARD;
            }
            else if (jClass == Float.TYPE) {
                spcPrimType = NumPrimitiveTypeDefn.REAL;
            }
            else if (jClass == Void.TYPE) {
                spcPrimType = VoidType.VOID;
            }

            this.isSpacePrim = spcPrimType != null && !jClass.isArray();

            // special cases for now: Java java.lang.String -> Space char sequence
            if (jClass == String.class) {
                spaceTypeRef =
                    getAstFactory().newTypeRef(jSourceInfo, Collections.singletonList(TypeRef.CollectionType.SEQUENCE));
                spaceTypeRef.setFirstPart(
                    getAstFactory().newMetaRefPart(spaceTypeRef, jSourceInfo, NumPrimitiveTypeDefn.CHAR.getName()));
            }
            else {
                String spcTypeRefName = spcPrimType != null ? spcPrimType.getName() : toSpaceTypeName(jClass);
                spaceTypeRef =
                    getAstFactory().newTypeRef(jSourceInfo, Collections.singletonList(javaToSpaceCollType(jClass)));
                spaceTypeRef.setFirstPart(
                    getAstFactory().newMetaRefPart(spaceTypeRef,
                                                   jSourceInfo,
                                                   spcTypeRefName));

            }
        }

        public boolean isSpacePrim() {
            return this.isSpacePrim;
        }

        public TypeRef getSpaceTypeRef() {
            return spaceTypeRef;
        }

        public PrimitiveTypeDefn getSpacePrimType() {
            return this.spcPrimType;
        }
    }

}
