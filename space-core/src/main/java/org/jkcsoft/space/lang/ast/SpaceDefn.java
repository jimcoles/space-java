/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */


package org.jkcsoft.space.lang.ast;

import org.jkcsoft.space.lang.runtime.RuntimeException;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Abstract base for EntityDefn and ViewDefn.
 *
 * @author Jim Coles
 * @version 1.0
 */
public class SpaceDefn extends ModelElement {

    private SpaceDefn               contextSpaceDefn;
    private List<CoordinateDefn>    coordinateList;
    private List<EquationDefn>      equations;
    private List<TransformDefn>     transformDefns;
    private List<AbstractActionDefn>    functionDefns = new LinkedList<>();

    // redundant
    private Map<String, CoordinateDefn>     indexCoordinatesByName = new HashMap<>();
    private Map<String, AbstractActionDefn> indexFunctionsByName = new HashMap<>();

    SpaceDefn(String name) {
        super(name);
    }

    void setContextSpaceDefn(SpaceDefn contextSpaceDefn) {
        this.contextSpaceDefn = contextSpaceDefn;
    }

    public SpaceDefn getContextSpaceDefn() {
        return contextSpaceDefn;
    }

    public boolean hasContextSpaceDefn() {
        return contextSpaceDefn != null;
    }

    public List<CoordinateDefn> getCoordinateList() {
        return coordinateList;
    }

    public CoordinateDefn addDimension(CoordinateDefn coordinateDefn) {
        if (coordinateList == null)
            coordinateList = new LinkedList<>();
        coordinateList.add(coordinateDefn);
        //
        indexCoordinatesByName.put(coordinateDefn.getName(), coordinateDefn);
        //
        addChild(coordinateDefn);
        return coordinateDefn;
    }

    public AbstractActionDefn getFunction(String name) {
        AbstractActionDefn abstractActionDefn = indexFunctionsByName.get(name);
        if (abstractActionDefn == null) {
            throw new RuntimeException("function ["+name+"] not found in " + this);
        }
        return abstractActionDefn;
    }

    public AbstractActionDefn addActionDefn(AbstractActionDefn actionDefn) {
        functionDefns.add(actionDefn);
        actionDefn.setContextSpaceDefn(this);
        //
        indexFunctionsByName.put(actionDefn.getName(), actionDefn);
        //
        addChild(actionDefn);
        return actionDefn;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SpaceDefn {" + getName() + ": ");
        for (AbstractActionDefn functionDefn: functionDefns) {
            sb.append(functionDefn.getName()+"(),");
        }
        return sb.toString();
    }
}
