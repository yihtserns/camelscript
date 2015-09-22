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
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;

/**
 *
 * @author yihtserns
 */
public class RouteBuilderExtension {

    /**
     * Chaining method calls to build route can get pretty unreadable or error prone:
     * <pre>
     * from('...').to('...').process {...}.transform(constant('Result')).process {...}.to('...')
     * from 'direct:input' to '...' process {...} transform(constant('Result')) process {...}.to('...')
     * </pre>
     * This enables an alternative:
     * <pre>
     * from 'direct:input' {
     *      to '...'
     *      process {...}
     *      transform constant('Result')
     *      process {...}
     *      to('...')
     * }
     * </pre>
     * @param self to start the route
     * @param uri the 'from' URI
     * @param defineRoutePrototype route definition after 'from'
     */
    public static void from(final RouteBuilder self, final String uri, final Closure defineRoutePrototype) {
        // Should not need to clone as it's unlikely that this closure will be invoked in multiple threads
        // (or even multiple times), but I want to keep this as a reference/reminder
        Closure defineRoute = (Closure) defineRoutePrototype.clone();

        RouteDefinition routeDefinition = self.from(uri);
        defineRoute.setDelegate(new ProcessorDefinitionBuilder(routeDefinition));
        defineRoute.setResolveStrategy(Closure.DELEGATE_FIRST);

        defineRoute.call();
    }
}
