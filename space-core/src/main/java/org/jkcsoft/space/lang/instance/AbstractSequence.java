/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2021 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

import org.jkcsoft.space.lang.ast.TypeDefn;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * The base class for all Java-level sequence classes, the instances of which back
 * Space sequences. A Space sequence is similar to the notion of an Array in other languages
 * in that it is an intrinsic collective notion with specific 'sequence operators'.
 * Space sequences may be used like arrays or like mutable linked lists. The Space
 * compiler and runtime will optimize the backing objects depending on a
 * sequence's actual or declared (intended) usage patterns.
 * A sequence allows random get/set access to its elements, but does not
 * allow a direct 'append' to increase the size of the sequence.
 *
 * @param <V> The Java type of the sequenced element. Must itself be a Java-level
 *           Space value type such as {@link ScalarValue} or {@link ReferenceValue}.
 *
 * @author Jim Coles
 */
public abstract class AbstractSequence<V extends Value> extends AbstractCollection<V> implements Value {

    private List<V> javaSequence = new LinkedList<>();

    public AbstractSequence(SpaceOid oid, TypeDefn defn) {
        super(oid, defn);
    }

    @Override
    public boolean isCollective() {
        return true;
    }

    @Override
    public boolean isTuple() {
        return false;
    }

    @Override
    public Object getJavaValue() {
        return javaSequence;
    }

    public AbstractSequence<V> add(V element) {
        javaSequence.add(element);
        return this;
    }

    public Iterator<V> getIterator() {
        return javaSequence.iterator();
    }
}
