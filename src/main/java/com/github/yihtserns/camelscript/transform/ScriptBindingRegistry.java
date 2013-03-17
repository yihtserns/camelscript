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
import java.util.Map;
import org.apache.camel.spi.Registry;

/**
 * Would prefer a {@code BindingRegistry} class, but after a Groovy Script is instantiated, a
 * {@link Script#setBinding(Binding) different binding will be set} - don't know enough about the lifecycle to
 * intercept just the {@link Binding}.
 *
 * @author yihtserns
 * @see #lookup(String)
 */
public class ScriptBindingRegistry implements Registry {

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
     * @return service from {@link Binding}, {@code null} if not found
     */
    public Object lookup(final String name) {
        return getBinding().hasVariable(name) ? getBinding().getVariable(name) : null;
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
     * Currently don't need this + and not sure how to properly implement.
     * @return empty Map
     */
    public <T> Map<String, T> lookupByType(final Class<T> type) {
        return Collections.emptyMap();
    }
}