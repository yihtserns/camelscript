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

package com.github.yihtserns.camelscript

import org.junit.Test
import org.apache.camel.CamelContext
import org.apache.camel.NoTypeConversionAvailableException
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.builder.RouteBuilder
import groovy.util.GroovyTestCase

/**
 * @author yihtserns
 */
class MessageCategoryTest {

    @Delegate(deprecated=true)
    private CamelContext camelContext = new DefaultCamelContext()
    def shouldFailWithCause = new GroovyTestCase().&shouldFailWithCause

    /**
     * Instead of returning null; I'd hate to debug those kind of things.
     */
    @Test
    void 'should throw when unable to convert'() {
        addRoutes(
            new RouteBuilder() {
                void configure() {
                    use (RouteDefinitionCategory, MessageCategory) {
                        from('direct:input').process {
                            it.in as Date
                        }
                    }
                }
            }
        )
        start()

        shouldFailWithCause(NoTypeConversionAvailableException) {
            createProducerTemplate().sendBody('direct:input', '2013-04-26')
        }
    }
}