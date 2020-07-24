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

import org.jkcsoft.space.lang.instance.CardinalValue;
import org.jkcsoft.space.lang.instance.RealValue;
import org.jkcsoft.space.lang.instance.ScalarValue;
import org.jkcsoft.space.lang.metameta.MetaType;

import java.util.Comparator;

/**
 * A general means of defining primary keys, alternate keys, and external keys.
 * Keys may be a simple single variable, opaque byte or as complex as tuple of scalar
 * variables.
 *
 * <p>4GL: Defining keys at the language-level moves Space in the 4GL direction.
 * Keys enable high-level language features:
 * <li>Language-managed indexes and trees.
 * <li>Mapping to key-oriented database models.
 * <li>CDI-like injection of objects.
 * <li>Simple binding of configuration data to related application objects.
 *
 * <p>Structurally, a {@link KeyDefnImpl} is just a {@link ViewDefn} that is constrained
 * to specifying only direct composite members of the basis type.
 *
 * @author Jim Coles
 */
public class KeyDefnImpl extends ViewDefnImpl {

    private static final Comparator<CardinalValue> CARD_COMP_ASC = Comparator.comparingLong(ScalarValue::getJavaValue);
    private static final Comparator<RealValue> REAL_COMP_ASC = Comparator.comparingDouble(ScalarValue::getJavaValue);
    private static final Comparator<String> STRING_COMP_ASC = String::compareTo;

    /**
     * @param sourceInfo
     * @param namePart optional name given that a type can have alternate keys
     * @param basisType
     * @param vars
     */
    protected KeyDefnImpl(SourceInfo sourceInfo, NamePart namePart, TypeDefn basisType, ProjectionDecl ... vars) {
        super(sourceInfo, namePart, basisType);
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.TYPE;
    }

}
