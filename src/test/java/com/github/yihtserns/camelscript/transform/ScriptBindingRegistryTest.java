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

import groovy.lang.Binding;
import groovy.lang.Script;
import java.util.Collections;
import org.apache.camel.processor.interceptor.Tracer;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author yihtserns
 */
public class ScriptBindingRegistryTest {

    private Binding binding = new Binding();
    private ScriptBindingRegistry registry;

    @Before
    public void initRegistry() throws Exception {
        Script script = new Script() {
            @Override
            public Object run() {
                throw new UnsupportedOperationException("Unused.");
            }
        };
        script.setBinding(binding);
        registry = new ScriptBindingRegistry(script);
    }

    @Test
    public void lookupShouldFindVariableInBinding() throws Exception {
        final String variableName = "myVariable";
        final Object variable = new Object();
        binding.setVariable(variableName, variable);

        assertThat(registry.lookup(variableName), is(variable));
    }

    @Test
    public void lookupWithTypeShouldFindVariableInBinding() throws Exception {
        final String variableName = "myVariable";
        final Object variable = "Expected Variable";
        binding.setVariable(variableName, variable);

        String result = registry.lookup(variableName, String.class);
        assertThat(result, is(variable));
    }

    @Test
    public void shouldReturnNullWhenVariableIsNotAvailableInBinding() throws Exception {
        assertThat(registry.lookup("NonExistentVariable"), is(nullValue()));
    }

    /**
     * Following {@link org.apache.camel.impl.JndiRegistry}'s behaviour.
     */
    @Test
    public void lookupByTypeShouldReturnEmptyMapToSatisfyContract() throws Exception {
        assertThat(registry.lookupByType(Tracer.class), is(Collections.<String, Tracer>emptyMap()));
    }
}