/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2016. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.metametamodel;

/**
 * Counterpart to IObjectRecord.  Whereas IObjectRecord enables generic loading
 * and saving of an Object, IObjectBuilder enables generic loading and traversal
 * of an entire object graph from a root element.
 * <p>
 * This interface will therefore be used by a dms.Persister for loading and
 * saving.  At the very least, will be used for loading and saving objects
 * within our dms to and from XML.
 * <p>
 * Analogous to XML DOM Builder.
 *
 * @author J. Coles
 * @version 1.0
 */
public interface IObjectBuilder {

}