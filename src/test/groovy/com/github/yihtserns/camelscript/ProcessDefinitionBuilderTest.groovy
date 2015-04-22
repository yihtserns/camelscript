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

import groovy.util.GroovyTestCase
import org.apache.camel.model.RouteDefinition
import org.junit.Test

/**
 * @author yihtserns
 */
class ProcessDefinitionBuilderTest {

    def shouldFail = new GroovyTestCase().&shouldFail

    @Test
    public void shouldThrowWhenNoSuchMethod() {
        def builder = new ProcessorDefinitionBuilder(new RouteDefinition())

        shouldFail(MissingMethodException) {
            builder.noSuchMethod()
        }
    }

    @Test
    public void wouldCallMethodMissingWhenNoSuchMethod() {
        def delegate = new RouteDefinition()
        delegate.metaClass.methodMissing = { String name, args ->
            println "HEEEEEEEEEEEEEEEEE"
        }

        def builder = new ProcessorDefinitionBuilder(delegate)
        builder.noSuchMethod()
    }
}
