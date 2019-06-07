/*
 * Copyright (c) Jim Coles (jameskcoles@gmail.com) 2019 through present.
 *
 * Licensed under the following license agreement:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Also see the LICENSE file in the repository root directory.
 */
package org.jkcsoft.space.lang.ast.sji;

import java.util.Map;
import java.util.TreeMap;

/**
 * A place to register binding from Java packages and classes to Space packages
 * and types.
 *
 * @author Jim Coles
 */
public class SjiBindings {

    private Map<String, String> javaToSpacePackages = new TreeMap<>();

    public void registerPToPBinding(String javaPackage, String spacePackage) {
        javaToSpacePackages.put(javaPackage, spacePackage);
    }

    public String getSpacePackage(String javaPackageFqn) {
        return javaToSpacePackages.get(javaPackageFqn);
    }

    /**
     * If javaClassName matches any binding, substitute the binding right-side for that matched
     * portion of the java class/package name.
     * @param javaClassName
     * @return
     */
    public String applyToJavaClassname(String javaClassName) {
        String spaceTypeName = javaClassName;
        for (Map.Entry<String, String> javaPackageBinding : javaToSpacePackages.entrySet()) {
            if (javaClassName.startsWith(javaPackageBinding.getKey())) {
                spaceTypeName = javaPackageBinding.getValue() +
                    javaClassName.substring(javaPackageBinding.getKey().length());
                break;
            }
        }
        return spaceTypeName;
    }

    public static class PackageToPackage {

        private String javaPackageFqn;
        private String spacePackageFqn;

        public PackageToPackage(String javaPackageFqn, String spacePackageFqn) {
            this.javaPackageFqn = javaPackageFqn;
            this.spacePackageFqn = spacePackageFqn;
        }
    }

}
