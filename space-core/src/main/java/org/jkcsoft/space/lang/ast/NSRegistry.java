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
import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Strings;
import org.jkcsoft.space.lang.runtime.AstUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * The central namespace registry: a point for registering named AST elements such as
 * Directories and Types. Holds the entire collection and enforces name uniqueness.
 *
 * @author Jim Coles
 */
public class NSRegistry {

    private static final Logger log = LoggerFactory.getLogger(NSRegistry.class);
    public static final String NS_TMP = "tmp";
    public static final String NS_USER = "user";
    public static final String NS_LANG = "lang";

    public static NSRegistry newInstance() {
        return new NSRegistry();
    }

    private static CommonCoreRegistry coreRegistry;

    // ------------------------------------------------------------------------
    //

    /**
     * Meta objects are loaded as we parse the source code. Intrinsic and native meta
     * objects are loaded prior to parsing and source files.  Must be able to lookup
     * a meta object by name or by id.  Some meta object are anonymous.
     */
    private final List<Namespace> nsChain = new LinkedList<>();
    private final List<Directory> rootDirs = new LinkedList<>();


    private Namespace tmpNs;
    private Namespace userNs;

    /**
     * The central meta object table.
     */
    private final java.util.Set<ModelElement> metaObjectNormalTable = new HashSet<>();

    /**
     * (not used currently) Idea is to hold redundantly accumulated info useful
     * for lookup during execution.
     */
//    private Map<NamedElement, MetaInfo> metaObjectExtendedInfoMap = new TreeMap<>();
    private NSRegistry() {
//        this.exeContext = exeContext;
        //
        AstFactory astFactory = AstFactory.getInstance();

        if (coreRegistry == null) {
            coreRegistry = new CommonCoreRegistry();
        }

        // add common core namespaces
        addNamespace(getJavaNs());
        addNamespace(getLangNs());

        // init dynamic context specific namespaces
        userNs = astFactory.newNamespace(SourceInfo.INTRINSIC,
                                         astFactory.newNamePart(SourceInfo.INTRINSIC, NS_USER),
                                         getLangNs());
        addNamespace(userNs);
        tmpNs = astFactory.newNamespace(SourceInfo.INTRINSIC,
                                        astFactory.newNamePart(SourceInfo.INTRINSIC, NS_TMP),
                                        userNs);
        addNamespace(tmpNs);

    }

    private void addNamespace(Namespace namespace) {
        nsChain.add(namespace);
        trackMetaObject(namespace);
        rootDirs.add(namespace.getRootDir());
    }

    public Namespace getJavaNs() {
        return coreRegistry.getJavaNs();
    }

    public Namespace getLangNs() {
        return coreRegistry.getLangNs();
    }

    public Namespace getUserNs() {
        return userNs;
    }

    public Namespace getTmpNs() {
        return tmpNs;
    }

    public Namespace getNamespace(String name) {
        return (Namespace) CollectionUtils.find(
            getNsChain(),
            elem -> ((Namespace) elem).getNamePart().getText().equals(name)
        );
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
        log.info("see dump log file for meta object table dump");
        Logger dumpLogger = getDumpLogger();
        dumpLogger.info("normalized meta object table: " + JavaHelper.EOL
                          + Strings.buildNewlineList(metaObjectNormalTable));
    }

    public void dumpAsts() {
        log.info("see dump log file for AST dumps");
        for (Namespace ns : getNsChain()) {
            //            log.debug("AST Root /: " + dump);
            getDumpLogger().info(AstUtils.printFullAst(ns));
            getFlatLogger().info(AstUtils.printFlatNamedAst(ns));
        }
    }

    private Logger getDumpLogger() {
        return LoggerFactory.getLogger("dumpFileLogger");
    }

    private Logger getFlatLogger() {
        return LoggerFactory.getLogger("flatAstDumpFileLogger");
    }

    private static class CommonCoreRegistry {
        private final Namespace langNs;
        private final Namespace javaNs;

        public CommonCoreRegistry() {
            AstFactory astFactory = AstFactory.getInstance();
            langNs = astFactory.newNamespace(SourceInfo.INTRINSIC,
                                             astFactory.newNamePart(SourceInfo.INTRINSIC, NS_LANG));
            javaNs = astFactory.newNamespace(SourceInfo.INTRINSIC,
                                             astFactory.newNamePart(SourceInfo.INTRINSIC, Language.JAVA.getCodeName()));
        }

        public Namespace getLangNs() {
            return langNs;
        }

        public Namespace getJavaNs() {
            return javaNs;
        }
    }
}
