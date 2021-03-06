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

/**
 * @author yihtserns
 */
@Mixin(CamelScriptCategory)
class CamelScriptCategoryTest {

    private CamelContext camelContext = new DefaultCamelContext()

    @Test
    void 'should be able to pass closure in as RouteBuilder'() {
        final def expectedResult = 'Result'

        routes {
            from('direct:input').transform(constant(expectedResult))
        }

        def result = camelContext.createProducerTemplate().requestBody('direct:input', (Object) null)

        assert result == expectedResult
    }
}