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
import java.util.Map;
import java.util.Set;

/**
 * @author Jim Coles
 */
public interface ModelElement extends Tagable {

    SourceInfo getSourceInfo();

    boolean hasParent();

    ModelElement getParent();

    void setParent(ModelElement modelElement);

    boolean isGroupingNode();

    void setGroupingNode(boolean groupingNode);

    boolean hasChildren();

    List<ModelElement> getChildren();

    AbstractNamedElement getChildByName(String name);

    Collection<AbstractNamedElement> getNamedChildren();

    Map<String, AbstractNamedElement> getNamedChildMap();

    boolean hasReferences();

    Set<ExpressionChain> getExpressionChains();

    /** This returns a short string that should be useful in log files. */
    String getDisplayName();

    boolean hasGroupingNodes();

    List<ModelElement> getGroupingNodes();

    Named getNamedParent();

    int getTreeDepth();

}
