/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2020 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.jlib.bindings;

import org.jkcsoft.java.util.JavaHelper;
import org.jkcsoft.java.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.List;
import java.util.Set;

/**
 * @author Jim Coles
 */
@SupportedAnnotationTypes("org.jkcsoft.space.runtime.jnative.binadings.NameBinding")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class AnnoProcessor extends AbstractProcessor {

    private static final Logger log = LoggerFactory.getLogger(AnnoProcessor.class);

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        log.info("process called with {} annotations", annotations.size());
        for (TypeElement anno : annotations) {
            Set<? extends Element> taggedElems = roundEnv.getElementsAnnotatedWith(anno);
            log.info("tagged: " + JavaHelper.EOL + Strings.buildNewlineList(taggedElems));
            for (Element taggedElem : taggedElems) {
                log.info("handling: " + taggedElem);
                List<? extends Element> packageChildren = taggedElem.getEnclosedElements();
                log.info("pack encl: " + Strings.buildNewlineList(packageChildren));
            }
        }
        return true;
    }
}
