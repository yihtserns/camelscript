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
package com.github.yihtserns.camelscript;

import groovy.grape.Grape;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.camel.CamelContext;
import org.apache.camel.Component;
import org.apache.camel.impl.DefaultComponentResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yihtserns
 * @see #resolveComponent(String, CamelContext)
 */
public class AutoGrabComponentResolver extends DefaultComponentResolver {

    private Logger log = LoggerFactory.getLogger(getClass());

    /**
     * If fail to resolve, will ask Groovy Grape to grab the required jar and try again.
     *
     * @param name component name
     * @param context to load the component
     * @return resolved component, {@code null} if cannot resolve
     */
    @Override
    public Component resolveComponent(final String name, final CamelContext context) {
        Component component = super.resolveComponent(name, context);
        if (component != null) {
            return component;
        }

        tryGrab(name, context);

        // Try again
        return super.resolveComponent(name, context);
    }

    private void tryGrab(final String name, final CamelContext context) {
        Map<String, String> dep
                = new LinkedHashMap<String, String>(); // looks nicer when calling toString()
        dep.put("group", "org.apache.camel");
        dep.put("module", resolveModuleName(name));
        dep.put("version", context.getVersion());

        log.info("Unable to resolve component '{}', asking Grape to grab it [{}]", name, dep);
        Grape.grab((Map) dep);
    }

    private String resolveModuleName(final String componentName) {
        String moduleNameSuffix = componentName;
        if ("https".equals(componentName)) {
            moduleNameSuffix = "http";
        }

        return "camel-" + moduleNameSuffix;
    }
}
