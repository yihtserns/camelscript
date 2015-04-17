/*
 * Copyright 2013 yihtserns.
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

import groovy.lang.Closure;
import java.util.Arrays;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.codehaus.groovy.runtime.GroovyCategorySupport;

/**
 * Groovy Category for {@link CamelContext}.
 *
 * @author yihtserns
 */
public class CamelContextCategory {

    /**
     * <strong>Usage example</strong>
     * <p/>
     * Instead of:
     * <pre>
     * camelContext.addRoutes(new RouteBuilder() {
     *     void configure() {
     *         from('direct:input')...
     *     }
     * })
     * </pre>
     * Can do this instead:
     * <pre>
     * use (CamelContextCategory) {
     *     camelContext.routes {
     *         from('direct:input')...
     *     }
     * }
     * </pre>
     * <strong>Note</strong>: {@link RouteDefinitionCategory} is auto-included to buildRoutePrototype
     * @param self to add routes to
     * @param buildRoutePrototype things you'd do in {@link RouteBuilder#configure()}
     * @throws Exception if an error occurs while building route
     * @see CamelContext#addRoutes(org.apache.camel.RoutesBuilder)
     */
    public static void routes(final CamelContext self, final Closure buildRoutePrototype) throws Exception {
        self.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                // Should not need to clone as it's unlikely that this closure will be invoked in multiple threads
                // (or even multiple times), but I want to keep this as a reference/reminder
                Closure buildRoute = (Closure) buildRoutePrototype.clone();

                buildRoute.setDelegate(this);
                GroovyCategorySupport.use(
                        Arrays.<Class>asList(RouteDefinitionCategory.class, RouteBuilderCategory.class),
                        buildRoute);
            }
        });
        self.start();
    }
}