/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2019 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @author Jim Coles
 */
public interface ModelElement {

    SourceInfo getSourceInfo();

    void setParent(ModelElement modelElement);

    boolean hasParent();

    ModelElement getParent();

    boolean isGroupingNode();

    void setGroupingNode(boolean groupingNode);

    boolean hasChildren();

    List<ModelElement> getChildren();

    NamedElement getChildByName(String name);

    Collection<NamedElement> getNamedChildren();

    boolean hasReferences();

    Set<ExpressionChain> getExpressionChains();

    String getDisplayName();

    boolean hasGroupingNodes();

    List<ModelElement> getGroupingNodes();

    Named getNamedParent();

    int getTreeDepth();
}
