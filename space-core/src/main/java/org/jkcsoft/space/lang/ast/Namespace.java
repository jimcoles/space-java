/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import org.jkcsoft.space.lang.instance.Tuple;
import org.jkcsoft.space.lang.metameta.MetaType;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * A named root of a Directory tree. A given runtime context will contain multiple Namespaces.
 * @author Jim Coles
 */
public class Namespace extends NamedElement {

    private Directory rootDir;
    private Namespace[] nsLookupChain;
    private Directory[] rootDirLookupChain;

    /** Derived mapping */
    private Map<TypeDefn, TypeDerivedInfo> typeInfoMap = new TreeMap<>();

    Namespace(SourceInfo sourceInfo, String name, Namespace ... nsLookupChain) {
        super(sourceInfo, name);
        this.rootDir = AstFactory.getInstance().newAstDir(new IntrinsicSourceInfo(), "(root)");
        this.nsLookupChain = new Namespace[nsLookupChain.length + 1];
        this.nsLookupChain[0] = this;
        for (int idxExtraLookup = 0; idxExtraLookup < nsLookupChain.length; idxExtraLookup++) {
            this.nsLookupChain[idxExtraLookup + 1] = nsLookupChain[idxExtraLookup];
        }
        this.rootDirLookupChain = new Directory[this.nsLookupChain.length];
        for (int idxChain = 0; idxChain < this.nsLookupChain.length; idxChain++) {
            this.rootDirLookupChain[idxChain] = this.nsLookupChain[idxChain].getRootDir();
        }
        //
        this.addChild(rootDir);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.PACKAGE;
    }

    public Directory getRootDir() {
        return rootDir;
    }

    public Namespace[] getNsLookupChain() {
        return nsLookupChain;
    }

    public Directory[] getRootDirLookupChain() {
        return rootDirLookupChain;
    }

    @Override
    public String getDisplayName() {
        return super.getDisplayName() + ":";
    }

    public void putTypeInfo(TypeDerivedInfo typeInfo) {
        typeInfoMap.put(typeInfo.getTypeDefn(), typeInfo);
    }

    public TypeDerivedInfo getTypeInfo(TypeDefn typeDefn) {
        return typeInfoMap.get(typeDefn);
    }

    public void setTypeInfo(TypeDefn typeDefn, TypeDerivedInfo typeInfo) {
        typeInfoMap.put(typeDefn, typeInfo);
    }

    public static class TypeLoadedEvent implements AstEvent {
        private TypeDefn typeDefn;

        public TypeLoadedEvent(TypeDefn typeDefn) {
            this.typeDefn = typeDefn;
        }

        public TypeDefn getTypeDefn() {
            return typeDefn;
        }
    }

    /** Holds type-related info computed and used by executor. */
    public static class TypeDerivedInfo {
        private TypeDefn typeDefn;
        private Comparator<Tuple> pkComparator;

        public TypeDerivedInfo(TypeDefn typeDefn) {
            this.typeDefn = typeDefn;
        }

        public TypeDefn getTypeDefn() {
            return typeDefn;
        }

        public void setPkComparator(Comparator<Tuple> pkComparator) {
            this.pkComparator = pkComparator;
        }

        public Comparator<Tuple> getPkComparator() {
            return pkComparator;
        }
    }

}
