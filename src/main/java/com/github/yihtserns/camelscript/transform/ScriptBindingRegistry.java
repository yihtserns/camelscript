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
import groovy.lang.Closure;
import groovy.lang.Script;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.camel.spi.Registry;

/**
 * Would prefer a {@code BindingRegistry} class, but after a Groovy Script is instantiated, a
 * {@link Script#setBinding(Binding) different binding will be set} - don't know enough about the lifecycle to intercept
 * just the {@link Binding}.
 *
 * @author yihtserns
 * @see #lookup(String)
 */
class ScriptBindingRegistry implements Registry {

    /**
     * @see #ScriptBindingRegistry(Script)
     */
    private Script script;

    /**
     * @param script just to get the {@link Binding}.
     * @see ScriptBindingRegistry
     */
    public ScriptBindingRegistry(final Script script) {
        this.script = script;
    }

    /**
     * @return service from {@link Binding}, (<strong>Note</strong>: {@link Closure#call() invocation result} if the
     * service is a closure), {@code null} if not found
     */
    public Object lookup(final String name) {
        if (!getBinding().hasVariable(name)) {
            return null;
        }
        Object service = getBinding().getVariable(name);
        if (service instanceof Closure) {
            return ((Closure) service).call();
        }
        return service;
    }

    /**
     * @see #lookup(String)
     */
    public Object lookupByName(final String name) {
        return lookup(name);
    }

    private Binding getBinding() {
        return script.getBinding();
    }

    /**
     * Delegates to {@link #lookup(String)}.
     */
    public <T> T lookup(final String name, final Class<T> type) {
        Object result = lookup(name);
        return type.cast(result);
    }

    /**
     * @see #lookup(String, Class)
     */
    public <T> T lookupByNameAndType(final String name, final Class<T> type) {
        return lookup(name, type);
    }

    /**
     * @return services from {@link Binding} that are compatible (same type or subtype) with the given type, empty
     * {@code Map} if none found
     */
    public <T> Map<String, T> lookupByType(final Class<T> type) {
        Map<String, T> varName2Var = new HashMap<String, T>();
        for (Entry<String, ?> entry : (Set<Entry>) script.getBinding().getVariables().entrySet()) {
            Object value = entry.getValue();
            if (type.isInstance(value)) {
                varName2Var.put(entry.getKey(), type.cast(value));
            }
        }

        return varName2Var;
    }

    /**
     * @see #lookupByType(Class)
     */
    public <T> Map<String, T> findByTypeWithName(Class<T> type) {
        return lookupByType(type);
    }

    /**
     * @see #findByTypeWithName(Class)
     */
    public <T> Set<T> findByType(Class<T> type) {
        return new HashSet<T>(findByTypeWithName(type).values());
    }
}
