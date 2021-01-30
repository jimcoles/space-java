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

import org.jkcsoft.space.lang.instance.ObjectFactory;
import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * A Directory is a organizational, namespace, and security mechanism similar
 * to an XML schema or Java Package.
 *
 * Encapsulates an entire executable system as defined by Space definition elements
 * (ModelElements) and associated instances.
 *
 * A Directory may contain child Directories or {@link TypeDefnImpl}'s or other {@link Named}
 * element types.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class Directory extends NamedElement {

//    public static final Directory ROOT_DIRECTORY = new Directory(new IntrinsicSourceInfo(), "root");

    private Namespace namespace;
    private Directory parentDir;
    private List<Directory> childDirectories = new LinkedList<>();
    private Set<ParseUnit> parseUnits = new HashSet<>();
//    private List<TypeDefn> typeDefns = new LinkedList<>();

    // ================== The starting point for using Space to execute Space programs

    // Uses Space constructs to hold a table (space) of SpaceOids to
    private ObjectFactory spaceBuilder = ObjectFactory.getInstance();

    // ==================

    Directory(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.PACKAGE;
    }

    // =========================================================================
    // Child adders

    public Directory addDir(Directory childDirectory) {
        childDirectories.add(childDirectory);
        //
        addChild(childDirectory);
        //
        return childDirectory;
    }

    public Directory getParentDir() {
        return parentDir;
    }

    public Namespace getNamespace() {
        return namespace;
    }

    public boolean isRootDir() {
        return getParentDir() == null;
    }

//    public TypeDefn addSpaceDefn(TypeDefn spaceTypeDefn) {
//        typeDefns.add(spaceTypeDefn);
//        //
//        addChild((NamedElement) spaceTypeDefn);
//        return spaceTypeDefn;
//    }

//    public StreamTypeDefn addStreamTypeDefn(StreamTypeDefn streamTypeDefn) {
//        addChild(streamTypeDefn);
//        return streamTypeDefn;
//    }

    public ParseUnit addParseUnit(ParseUnit parseUnit) {
        parseUnits.add(parseUnit);
        //
        addChild(parseUnit);
        //
        return parseUnit;
    }

    public boolean hasChildDirs() {
        return childDirectories != null && childDirectories.size() > 0;
    }

    public List<Directory> getChildDirectories() {
        return childDirectories;
    }

    public Set<ParseUnit> getParseUnits() {
        return parseUnits;
    }

//    public List<TypeDefn> getTypes() {
//        return typeDefns;
//    }

    @Override
    public void setParent(ModelElement parent) {
        if (parent instanceof Directory)
            this.parentDir = (Directory) parent;

        if (parent instanceof Namespace) {
            this.namespace = ((Namespace) parent);
        }
        //
        super.setParent(parent);
    }

    public Directory getChildDir(String name) {
        Directory childDir = null;
        NamedElement childElem = getChildByName(name);
        if (childElem instanceof Directory)
            childDir = ((Directory) childElem);
        return childDir;
    }

}
