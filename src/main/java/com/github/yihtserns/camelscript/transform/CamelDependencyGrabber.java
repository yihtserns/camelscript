/*
 * Copyright 2015 yihtserns.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.yihtserns.camelscript.transform;

import groovy.grape.Grape;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.camel.CamelContext;

/**
 * @author yihtserns
 * @see #require(java.lang.String...)
 */
class CamelDependencyGrabber {

    private String camelVersion;

    /**
     * @param camelContext to get the {@link CamelContext#getVersion() version}
     */
    public CamelDependencyGrabber(CamelContext camelContext) {
        this.camelVersion = camelContext.getVersion();
    }

    /**
     * Grab specified camel modules.
     *
     * @param moduleNames e.g. {@code jetty} for {@code camel-jetty}, {@code twitter} for {@code camel-twitter},
     * and so on
     * @see Grape#grab(java.util.Map, java.util.Map...)
     */
    public void require(String... moduleNames) {
        List<Map<String, Object>> dependencies = new ArrayList<Map<String, Object>>();

        for (String moduleName : moduleNames) {
            Map<String, Object> artifact = new HashMap<String, Object>();
            artifact.put("group", "org.apache.camel");
            artifact.put("module", "camel-" + moduleName);
            artifact.put("version", camelVersion);

            dependencies.add(artifact);
        }

        Grape.grab(new HashMap<String, Object>(), dependencies.toArray(new Map[dependencies.size()]));
    }
}
