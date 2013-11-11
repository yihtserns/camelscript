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
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.model.RouteDefinition;
import org.codehaus.groovy.runtime.GroovyCategorySupport;

/**
 * Groovy Category for {@link RouteDefinition}s.
 *
 * @author yihtserns
 */
public class RouteDefinitionCategory {

    /**
     * <strong>Usage example</strong>
     * <p/>
     * Instead of:
     * <pre>
     * from('direct:input').process(new Processor() {
     *     void process(Exchange exchange) {
     *         exchange.out.body = 'Result'
     *     }
     * })
     * </pre>
     * or:
     * <pre>
     * from('direct:input').process ({ exchange -> exchange.out.body = 'Result' } as Processor)
     * </pre>
     * Can do this instead:
     * <pre>
     * use (RouteDefinitionCategory) {
     *     from('direct:input').process { exchange -> exchange.out.body = 'Result' }
     * }
     * </pre>
     *
     * @param self to add {@link Processor} closure to
     * @param process {@link Processor} closure
     * @return result of {@link RouteDefinition#process(Processor)}
     * @see org.apache.camel.builder.RouteBuilder#from(String)
     * @see Processor
     */
    public static RouteDefinition process(final RouteDefinition self, final Closure<Void> process) {
        return self.process(new ClosureProcessor(process));
    }

    public static RouteDefinition transform(final RouteDefinition self, final Closure<Object> transform) {
        return self.transform(new ClosureExchangeTransformer(transform));
    }

    private static final class ClosureProcessor implements Processor {

        private Closure<Void> process;

        public ClosureProcessor(final Closure<Void> process) {
            this.process = process;
        }

        public void process(final Exchange exchange) throws Exception {
            GroovyCategorySupport.use(MessageCategory.class, new Closure(null) {
                public void doCall() {
                    process.call(exchange);
                }
            });
        }
    }

    private static final class ClosureExchangeTransformer implements Expression {

        private Closure<Object> transform;

        public ClosureExchangeTransformer(Closure<Object> transform) {
            this.transform = transform;
        }

        public Object evaluate(final Exchange exchange, Class unused) {
            return GroovyCategorySupport.use(MessageCategory.class, transform.curry(exchange));
        }
    }
}