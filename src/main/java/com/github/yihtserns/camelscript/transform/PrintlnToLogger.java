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

import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.runtime.InvokerHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Using this and static import to redirect println to log.
 * <p/>
 * <strong>Note</strong>: This is temporary solution, until I find out how to use the script's class name as
 * category.
 *
 * @author yihtserns
 */
public class PrintlnToLogger {

    private static Logger log = LoggerFactory.getLogger(PrintlnToLogger.class);

    public static void println(final Object obj) {
        log.info(InvokerHelper.toString(obj));
    }

    public static void print(final Object obj) {
        println(obj);
    }

    public static void println() {
        println("");
    }

    public static void printf(final String format, final Object values) {
        println(DefaultGroovyMethods.sprintf(null, format, values));
    }
}