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

/**
 * Help translate print, println & printf to logger.
 *
 * @author yihtserns
 */
public class PrintlnToLogger {

    private Logger log;

    public PrintlnToLogger(final Logger log) {
        this.log = log;
    }

    public void println(final Object obj) {
        log.info(InvokerHelper.toString(obj));
    }

    public void print(final Object obj) {
        println(obj);
    }

    public void println() {
        println("");
    }

    public void printf(final String format, final Object values) {
        println(DefaultGroovyMethods.sprintf(null, format, values));
    }

    public void printf(final String format, final Object[] values) {
        printf(format, (Object) values);
    }
}