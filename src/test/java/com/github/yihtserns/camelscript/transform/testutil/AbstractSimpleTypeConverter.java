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
package com.github.yihtserns.camelscript.transform.testutil;

import org.apache.camel.Exchange;
import org.apache.camel.NoTypeConversionAvailableException;
import org.apache.camel.TypeConverter;

/**
 * Convenience base class so you'd only need to implement {@link TypeConverter#mandatoryConvertTo(Class, Object)}.
 *
 * @author yihtserns
 */
public abstract class AbstractSimpleTypeConverter implements TypeConverter {

    /**
     * Delegates to {@link #mandatoryConvertTo(Class, Object)}, returning {@code null} if
     * {@link NoTypeConversionAvailableException} is thrown.
     */
    @Override
    public final <T> T convertTo(Class<T> type, Object value) {
        try {
            return mandatoryConvertTo(type, value);
        } catch (NoTypeConversionAvailableException ex) {
            return null;
        }
    }

    /**
     * Delegates to {@link #convertTo(Class, Object)}.
     */
    @Override
    public final <T> T convertTo(Class<T> type, Exchange exchange, Object value) {
        return convertTo(type, value);
    }

    /**
     * Delegates to {@link #mandatoryConvertTo(Class, Object)}.
     */
    @Override
    public final <T> T mandatoryConvertTo(Class<T> type, Exchange exchange, Object value) throws NoTypeConversionAvailableException {
        return mandatoryConvertTo(type, value);
    }

    // == Future proofing ==
    // Below are methods introduced in TypeConverter by CAMEL-5172.
    // Adding these "future" methods so can compile with newer Camel without modification.
    // ==
    /**
     * Delegates to {@link #convertTo(Class, Object)}, returning {@code null} if {@link RuntimeException} is thrown.
     */
    public final <T> T tryConvertTo(Class<T> type, Object value) {
        try {
            return convertTo(type, value);
        } catch (RuntimeException ex) {
            return null;
        }
    }

    /**
     * Delegates to {@link #tryConvertTo(Class, Object)}.
     */
    public final <T> T tryConvertTo(Class<T> type, Exchange exchange, Object value) {
        return tryConvertTo(type, value);
    }

    /**
     * @return {@code false}
     */
    public boolean allowNull() {
        return false;
    }
}
