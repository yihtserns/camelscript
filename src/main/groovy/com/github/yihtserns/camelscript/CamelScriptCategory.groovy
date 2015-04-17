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

/**
 *
 * @author yihtserns
 */
@Category(Object)
class CamelScriptCategory {

    /**
     * Assumes the mixin target has field/property {@code camelContext}, delegates that and given closure to
     * CamelContextCategory#routes(CamelContext, Closure).
     */
	def routes(Closure buildRoute) {
        CamelContextCategory.routes(camelContext, buildRoute)
    }

    /**
     * Some routes do not block - e.g. 'file:' ends immediately after starting.
     * This is a convenience method to stop the script from exiting.
     */
    def waitForever() {
        Thread.currentThread().join()
    }
}

