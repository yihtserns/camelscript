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

import org.apache.camel.impl.DefaultCamelContext;
import org.junit.Test;

/**
 * To verify whether Camel version is actually overridden in Travis.
 *
 * @author yihtserns
 */
public class CamelVersionTest {

    @Test
    public void printCamelVersion() {
        System.out.println("Camel version: " + new DefaultCamelContext().getVersion());
    }
}
