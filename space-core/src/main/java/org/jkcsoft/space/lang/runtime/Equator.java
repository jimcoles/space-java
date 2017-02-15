/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2017. through present.
 *
 * Licensed under the following license agreement:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

package org.jkcsoft.space.lang.runtime;

/**
 * The Equator's job is, upon any state change, to evaluate all
 * Equations to determine if other state must also change in order
 * to keep the Equation value 'true'.  If I can get this to work,
 * it may be possible to do away with user-managed assignment operations.
 *
 * Issues that I foresee:
 *
 * - Many equations do not imply unique mappings.  So, perhaps, disallow setting
 * of any variable for which this ambiguity applies.
 *
 * - Handle cases of
 *  - symbolic versus discretely enumerated equations
 *  - non-equalities: <, >
 *
 * @author Jim Coles
 */
public class Equator {
}
