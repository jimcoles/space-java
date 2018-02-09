/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2018 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */

/**
 * Defines the virtual processor (machine language) upon which Space runs.
 * This language is a sibling to any machine language such as that of the
 * x86 processor or Motorola processor.
 *
 * At some point we'll have the ability to compile to this binary form and
 * to load from the binary form of the language.
 *
 * We might make the binary derive from the textual form in the manner that
 * GPBs have both a textual and binary form.
 *
 * @author Jim Coles
 */
package org.jkcsoft.space.sprocket;