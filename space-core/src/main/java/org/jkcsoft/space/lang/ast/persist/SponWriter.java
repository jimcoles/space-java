/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast.persist;

import org.apache.commons.io.FileUtils;
import org.jkcsoft.space.lang.ast.AssociationDefn;
import org.jkcsoft.space.lang.ast.DatumProjectionExpr;
import org.jkcsoft.space.lang.ast.VariableDecl;
import org.jkcsoft.space.lang.ast.ViewDefn;
import org.jkcsoft.space.lang.instance.*;

import java.io.*;
import java.util.List;

/**
 * Visits every Space node writes out to a simple text format that looks
 * a bit like JSON or GPB text format.
 *
 * @author Jim Coles
 */
public class SponWriter {

    public static final String SET_BEGIN = "{";
    public static final String SET_END = "}";
    public static final String SEQ_BEGIN = "[";
    public static final String SEQ_END = "]";

    private Space space;
    private ViewDefn writeTree;
    private DatumProjectionExpr currentTreePos;

    public void writeSpon(ReferenceValue indexRef) {
        List<View> allIndices = space.getViews();
    }

    public void write(SpaceObject spObj) throws IOException {
        if (spObj.isCollective()) {
            ObjectRefCollection spColl = (ObjectRefCollection) spObj;
            // TODO Handle collection objects
            if (spColl.isSequence()) {
                var spSeq = (ObjectRefSequence) spColl;
                for (Object o : spSeq) {

                }
            }
            else if (spColl.isSet()) {

            }
        }
        else if (spObj.isTuple()) {
            var tuple = ((Tuple) spObj);
            var defn = tuple.getDefn();
            var sponFile = FileUtils.getFile(defn.getNamePart() + "-spon");
            write(new FileWriter(sponFile), ((Tuple) spObj));
        }
    }

    public void write(FileWriter fw, Tuple tuple) throws IOException {
        var defn = tuple.getDefn();
        fw.append(SET_BEGIN + defn.getNamePart());
        for (VariableDecl variableDecl : defn.getVariablesDeclList()) {
            append(fw, tuple, variableDecl);
        }
        fw.append(SET_END);
    }

    private void append(FileWriter fw, Tuple tuple, VariableDecl variableDecl) throws IOException {
        fw.append(SET_BEGIN + variableDecl.getNamePart() + " ");
        fw.append(tuple.get(variableDecl).getValue().toString());
        fw.append(SET_END);
    }

    private void append(FileWriter fw, Tuple tuple, AssociationDefn assocDefn) throws IOException {
        fw.append(getBeginSymbol() + assocDefn.getFromEnd().getDatumDecl().getName() + " ");
        if (assocDefn.getToEnd().isSingular()) {

        }
        else {

        }
        fw.append(tuple.get(assocDefn.getFromEnd().getDatumDecl()).getValue().toString());
        fw.append(SET_END);
    }

    private String getBeginSymbol() {
        return SET_BEGIN;
    }

}
