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
import org.apache.camel.Predicate;
import org.apache.camel.Processor;
import org.apache.camel.model.FilterDefinition;
import org.apache.camel.model.ProcessorDefinition;
import org.codehaus.groovy.runtime.GroovyCategorySupport;

/**
 * Groovy Category for {@link org.apache.camel.model.RouteDefinition}s.
 *
 * TODO: No longer a RouteDefinitionCategory, learn that should've been a ProcessorDefinitionCategory instead.
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
     * @return result of {@link org.apache.camel.model.RouteDefinition#process(Processor)}
     * @see org.apache.camel.builder.RouteBuilder#from(String)
     * @see Processor
     */
    public static ProcessorDefinition process(final ProcessorDefinition self, final Closure<Void> process) {
        return self.process(new ClosureProcessor(process));
    }

    public static ProcessorDefinition transform(final ProcessorDefinition self, final Closure<Object> transform) {
        return self.transform(new ClosureExchangeTransformer(transform));
    }

    public static FilterDefinition filter(final ProcessorDefinition self, final Closure<Boolean> predicate) {
        return self.filter(new ClosurePredicate(predicate));
    }

    private static final class ClosureProcessor implements Processor {

        private Closure<Void> process;

        public ClosureProcessor(final Closure<Void> process) {
            this.process = process;
        }

        public void process(final Exchange exchange) throws Exception {
            GroovyCategorySupport.use(MessageCategory.class, process.curry(exchange));
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

    private static final class ClosurePredicate implements Predicate {

        private Closure<Boolean> predicate;

        public ClosurePredicate(Closure<Boolean> predicate) {
            this.predicate = predicate;
        }

        public boolean matches(Exchange exchange) {
            return GroovyCategorySupport.use(MessageCategory.class, predicate.curry(exchange));
        }
    }
}