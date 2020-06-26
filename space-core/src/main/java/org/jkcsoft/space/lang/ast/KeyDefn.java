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

import org.jkcsoft.space.lang.metameta.MetaType;

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
 * @author Jim Coles
 */
public class KeyDefn extends NamedElement {

    /** Must be a simple list of vars of a single root type. */
    private TypeDefn vars;

    protected KeyDefn(SourceInfo sourceInfo, String name) {
        super(sourceInfo, name);
    }

    public TypeDefn getVars() {
        return vars;
    }

    @Override
    public MetaType getMetaType() {
        return MetaType.TYPE;
    }
}
