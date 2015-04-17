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

import org.apache.camel.Message;
import org.apache.camel.NoTypeConversionAvailableException;

/**
 *
 * @author yihtserns
 */
public class MessageExtension {

    public static Object asType(final Message self, final Class type) throws NoTypeConversionAvailableException {
        Object convertedBody = self.getBody(type);
        if (convertedBody == null) {
            throw new NoTypeConversionAvailableException(self, type);
        }
        return convertedBody;
    }
}