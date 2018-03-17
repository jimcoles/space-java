/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.instance;

/**
 * {@link ExeContext}s hold all user data that must be available to expression evaluators.
 * The context chain includes local vars, function argument values, 'this' object values
 * and public static type values.
 *
 * <p>IDEAS:
 * <br>- May also include the 'persistent' contexts such as database tables?
 * <br>- May include 'boundary' notions such as external requests.
 * <br>- May include 'transaction' context objects.
 *
 * @author Jim Coles
 */
public interface ExeContext {

}
