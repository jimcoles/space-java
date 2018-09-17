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

import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.SpaceHome;
import org.jkcsoft.space.lang.runtime.AstUtils;

import java.util.*;

/**
 * The central namespace registry: a point for registering named AST elements such as
 * Directories and Types. Holds the entire collection and enforces name uniqueness.
 *
 * @author Jim Coles
 */
public class NSRegistry {
    private static final Logger log = Logger.getLogger(NSRegistry.class);

    private static NSRegistry instance;

    public static NSRegistry getInstance() {
        if (instance == null)
            instance = new NSRegistry();

        return instance;
    }

    /**
     * Meta objects are loaded as we parse the source code. Intrinsic and native meta
     * objects are loaded prior to parsing and source files.  Must be able to lookup
     * a meta object by name or by id.  Some meta object are anonymous.
     */
    private final List<Namespace> nsChain = new LinkedList<>();
    private final List<Directory> rootDirs = new LinkedList<>();

    /**
     * A special directory root to hold intrinsic operators.
     */
    private Namespace langNs;
    private Namespace tmpNs;
    private Namespace userNs;
    private Namespace javaNs;

    private Named root;
    private Map<String, Named> allNamed = new TreeMap<>();
    /**
     * The central meta object table.
     */
    private java.util.Set metaObjectNormalTable = new HashSet<>();

    /**
     * (not used currently) Idea is to hold redundantly accumulated info useful
     * for lookup during execution.
     */
//    private Map<NamedElement, MetaInfo> metaObjectExtendedInfoMap = new TreeMap<>();
    private NSRegistry() {
        AstFactory astFactory = SpaceHome.getAstFactory();
        langNs = astFactory.newNamespace(SourceInfo.INTRINSIC, "lang");
        addNamespace(langNs);
        userNs = astFactory.newNamespace(SourceInfo.INTRINSIC, "user", langNs);
        addNamespace(userNs);
        javaNs = astFactory.newNamespace(SourceInfo.INTRINSIC, Language.JAVA.getCodeName());
        addNamespace(javaNs);
        tmpNs = astFactory.newNamespace(SourceInfo.INTRINSIC, "tmp", userNs);
        addNamespace(tmpNs);
    }

    public void addNamespace(Namespace namespace) {
        nsChain.add(namespace);
        trackMetaObject(namespace);
        rootDirs.add(namespace.getRootDir());
    }

    public Namespace getUserNs() {
        return userNs;
    }

    public Namespace getJavaNs() {
        return javaNs;
    }

    public Namespace getLangNs() {
        return langNs;
    }

    public Namespace getTmpNs() {
        return tmpNs;
    }

    public Namespace getNamespace(String name) {
        return (Namespace) CollectionUtils.find(getNsChain(), elem -> ((Namespace) elem).getName().equals(name));
    }

    public void addElement(Named newElement) {
        newElement.getNamedParent();
    }

    public List<Namespace> getNsChain() {
        return nsChain;
    }

    public List<Directory> getRootDirs() {
        return rootDirs;
    }

    // -------------- Tracking of runtime/instance things ---------------
    public void trackMetaObject(ModelElement modelElement) {
        // Add to normalized object table ...
        metaObjectNormalTable.add(modelElement);
//        metaObjectIndexByFullPath.put(modelElement.getFullPath(), modelElement);

        // track denormalized info for fast lookup ...
//        if (modelElement instanceof NamedElement) {
//            MetaInfo metaInfo = new MetaInfo((NamedElement) modelElement);
//            metaObjectExtendedInfoMap.put((NamedElement) modelElement, metaInfo);
//
//            // if this object has a parent, Add this object to parent's child map
//            if (modelElement.hasParent()) {
//                metaObjectExtendedInfoMap.get(modelElement.getParent()).addChild((NamedElement) modelElement);
//            }
//        }
    }

    public void dumpSymbolTables() {
        log.debug("normalized meta object table: " + JavaHelper.EOL
                      + Strings.buildNewlineList(metaObjectNormalTable));
    }

    public void dumpAsts() {
        log.info("see dump log file for AST dumps");
        for (Namespace ns : getNsChain()) {
            String dump = AstUtils.print(ns);
//            log.debug("AST Root /: " + dump);
            Logger.getLogger("dumpFile").info(dump);
        }
    }

}
