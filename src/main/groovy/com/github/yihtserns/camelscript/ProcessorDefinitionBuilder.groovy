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

import groovy.lang.MissingMethodException
import org.apache.camel.model.ProcessorDefinition

/**
 * @author yihtserns
 * @see #invokeMethod(String, Object)
 */
class ProcessorDefinitionBuilder {

    def ProcessorDefinition currentDelegate

    ProcessorDefinitionBuilder(ProcessorDefinition initialDelegate) {
        this.currentDelegate = initialDelegate
    }

    /**
     * First of all, this:
     * <pre>
     * from(...).xxx(...).yyy(...).zzz(...)
     * </pre>
     * is not the same as:
     * <pre>
     * def from = from(...)
     * from.xxx(...)
     * from.yyy(...)
     * from.zzz(...)
     * </pre>
     *
     * To allow writing the former as:
     * <pre>
     * from(...) {
     *     xxx(...) // == delegate.xxx(...)
     *     yyy(...) // == delegate.yyy(...)
     *     zzz(...) // == delegate.yyy(...)
     * }
     * </pre>
     * the {@code delegate} needs to replaced with result of builder method
     * (e.g. {@code process}, {@code transform}, {@code filter}) invocation so that instead of this:
     * <pre>
     * from(...) {
     *     resultOfFrom.xxx(...)
     *     resultOfFrom.yyy(...)
     *     resultOfFrom.zzz(...)
     * }
     * </pre>
     * we get:
     * <pre>
     * from(...) {
     *     resultOfFrom.xxx(...)
     *     resultOfXXX.yyy(...)
     *     resultOfYYY.zzz(...)
     * }
     * </pre>
     */
    Object invokeMethod(String name, Object args) {
        def mc = currentDelegate.metaClass

        def method = mc.getMetaMethod(name, args)
        if (method == null) {
            return mc.invokeMissingMethod(currentDelegate, name, args)
        }

        def result = method.doMethodInvoke(currentDelegate, args)
        if (result instanceof ProcessorDefinition) {
            // Builder method called
            currentDelegate = result
            return this
        }

        // Non-builder method called
        return result
    }
}