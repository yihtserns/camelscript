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

package com.github.yihtserns.camelscript

import org.apache.camel.builder.RouteBuilder

/**
 * Groovy Category for Objects that contains field/property {@code camelContext} of type {@link org.apache.camel.CamelContext}.
 *
 * @author yihtserns
 */
@Category(Object)
class CamelScriptCategory {

    /**
     * @param buildRoutePrototype things you'd do in {@link RouteBuilder#configure()}
     * @see org.apache.camel.CamelContext#addRoutes(org.apache.camel.RoutesBuilder)
     */
	def routes(Closure buildRoutePrototype) {
        RouteBuilder routeBuilder

        // Using closure because facing cryptic problem with anonymous class here
        routeBuilder = {
            // Should not need to clone as it's unlikely that this closure will be invoked in multiple threads
            // (or even multiple times), but I want to keep this as a reference/reminder
            Closure buildRoute = (Closure) buildRoutePrototype.clone();
            buildRoute.setDelegate(routeBuilder);

            buildRoute.call();
        }

        camelContext.addRoutes(routeBuilder);
        camelContext.start();
    }

    /**
     * Some routes do not block - e.g. 'file:' ends immediately after starting.
     * This is a convenience method to stop the script from exiting.
     */
    def waitForever() {
        Thread.currentThread().join()
    }
}

