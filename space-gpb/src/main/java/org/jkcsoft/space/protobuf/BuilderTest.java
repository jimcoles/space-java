/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.protobuf;

import org.jkcsoft.space.Space;

/**
 * @author Jim Coles
 */
public class BuilderTest {

    public static void main(String[] args) {
        Space.SpaceProgramDefn spaceProgramDefn = Space.SpaceProgramDefn.newBuilder().addSpaces(
            Space.SpaceDefn.newBuilder()
                .setName("MyProtoSpace")
                .addCoordinates(
                    Space.CoordinateDefn.newBuilder()
                        .setName("myDim")
                        .setType(Space.ScalarType.INT)
                )
                .addCoordinates(
                    Space.CoordinateDefn.newBuilder()
                        .setName("myDim2")
                        .setType(Space.ScalarType.BOOLEAN)
                )
                .addFunctions(
                    Space.ActionDefn.newBuilder()
                        .setName("main")
                        .addSubCallActions(
                            Space.CallActionDefn.newBuilder()
                                .setRefFunctionName("opsys.println")))
        )
            .build();
        System.out.println(spaceProgramDefn);
    }
}
