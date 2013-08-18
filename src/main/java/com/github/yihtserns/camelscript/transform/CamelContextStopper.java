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
package com.github.yihtserns.camelscript.transform;

import org.apache.camel.CamelContext;

/**
 * @author yihtserns
 * @see #run()
 * @see #registerToShutdownHook(CamelContext)
 */
class CamelContextStopper implements Runnable {

    private CamelContext context;

    /**
     * Use {@link #registerToShutdownHook(CamelContext)} instead.
     */
    CamelContextStopper(final CamelContext context) {
        this.context = context;
    }

    /**
     * Calls {@link CamelContext#stop()}.
     *
     * @throws RuntimeException to wrap exception (if) thrown by {@link CamelContext#stop()}
     */
    public void run() {
        try {
            context.stop();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void registerToShutdownHook(final CamelContext context) {
        Runtime.getRuntime().addShutdownHook(new Thread(new CamelContextStopper(context)));
    }
}