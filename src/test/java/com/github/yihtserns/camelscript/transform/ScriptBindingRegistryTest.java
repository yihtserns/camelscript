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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
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
        assertThat(registry.lookupByName(variableName), is(variable));
    }

    @Test
    public void lookupWithTypeShouldFindVariableInBinding() throws Exception {
        final String variableName = "myVariable";
        final Object variable = "Expected Variable";
        binding.setVariable(variableName, variable);

        assertThat(registry.lookup(variableName, String.class), is(variable));
        assertThat(registry.lookupByNameAndType(variableName, String.class), is(variable));
    }

    @Test
    public void shouldReturnNullWhenVariableIsNotAvailableInBinding() throws Exception {
        assertThat(registry.lookup("NonExistentVariable"), is(nullValue()));
    }

    @Test
    public void lookupByTypeShouldFindVariablesOfGivenTypeInBinding() throws Exception {
        Map<String, Integer> integerVariables = new HashMap<String, Integer>();
        Map<String, String> stringVariables = new HashMap<String, String>();
        integerVariables.put("firstInteger", 3);
        integerVariables.put("secondInteger", 5);
        stringVariables.put("firstString", "10");

        copyInto(binding,
                integerVariables,
                stringVariables);

        assertThat(registry.lookupByType(Integer.class), is(integerVariables));
        assertThat(registry.lookupByType(String.class), is(stringVariables));

        assertThat(registry.findByTypeWithName(Integer.class), is(integerVariables));
        assertThat(registry.findByTypeWithName(String.class), is(stringVariables));

        assertThat(registry.findByType(Integer.class), is(newSet(integerVariables.values())));
        assertThat(registry.findByType(String.class), is(newSet(stringVariables.values())));
    }

    @Test
    public void lookupByTypeShouldIncludeVariableOfSubtype() throws Exception {
        String variableName = "stringVariable";
        CharSequence variable = "Expected Variable";
        binding.setVariable(variableName, variable);

        assertThat(registry.lookupByType(CharSequence.class), hasEntry(variableName, variable));
        assertThat(registry.findByTypeWithName(CharSequence.class), hasEntry(variableName, variable));
        assertThat(registry.findByType(CharSequence.class), is(Collections.singleton(variable)));
    }

    private static void copyInto(Binding binding, Map<String, ?>... nVariables) {
        for (Map<String, ?> variables : nVariables) {
            for (Entry<String, ?> entry : variables.entrySet()) {
                binding.setVariable(entry.getKey(), entry.getValue());
            }
        }
    }

    private static <T> Set<T> newSet(Collection<T> collection) {
        return new HashSet<T>(collection);
    }
}
