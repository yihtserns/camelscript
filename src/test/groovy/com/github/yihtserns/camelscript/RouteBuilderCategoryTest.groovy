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
import org.apache.camel.impl.DefaultCamelContext
import org.apache.camel.builder.RouteBuilder

/**
 * @author yihtserns
 */
class RouteBuilderCategoryTest {

    @Delegate(deprecated=true)
    private CamelContext camelContext = new DefaultCamelContext()

    @Test
    void "should be able to chain next line's process calls"() {
        addRoutes(
            new RouteBuilder() {
                void configure() {
                    use (RouteBuilderCategory) {
                        from('direct:input') {
                            process {it.out.body = 'Res'}
                            process {it.out.body = "${it.in.body}ult"}
                        }
                    }
                }
            }
        )
        start()

        def result = createProducerTemplate().requestBody('direct:input', (Object) null)
        assert result == 'Result'
    }

    /**
     * Don't know enough to make the comma unnecessary, but I have plans to make the 'from' optional anyway
     * so don't want to spend too much time on this.
     */
    @Test
    void "from without parenthesis"() {
        addRoutes(
            new RouteBuilder() {
                void configure() {
                    use (RouteBuilderCategory) {
                        from 'direct:input', { // GOTCHA: Note the comma
                            process {it.out.body = 'Res'}
                            process {it.out.body = "${it.in.body}ult"}
                        }
                    }
                }
            }
        )
        start()

        def result = createProducerTemplate().requestBody('direct:input', (Object) null)
        assert result == 'Result'
    }
}