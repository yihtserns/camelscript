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

import org.junit.Test;
import org.slf4j.Logger;
import static org.mockito.Mockito.*;

/**
 * @author yihtserns
 */
public class PrintToLoggerTest {

    private Logger logger = mock(Logger.class);
    private PrintToLogger printToLogger = new PrintToLogger(logger);

    @Test
    public void shouldLogFormattedObjectPassedToPrintln() {
        printToLogger.println(new String[]{"A", "B", "C"});

        verify(logger).info("[A, B, C]");
    }

    @Test
    public void shouldLogFormattedObjectPassedToPrint() throws Exception {
        printToLogger.print(new String[]{"A", "B", "C"});

        verify(logger).info("[A, B, C]");
    }

    @Test
    public void shouldLogFormattedObjectPassedToPrintf() throws Exception {
        printToLogger.printf("A B %s", "C");

        verify(logger).info("A B C");
    }

    @Test
    public void shouldLogFormattedObjectPassedToMultiArgsPrintf() throws Exception {
        printToLogger.printf("A %s %s", new String[]{"B", "C"});

        verify(logger).info("A B C");
    }

    @Test
    public void shouldLogEmptyStringForNoArgPrintln() throws Exception {
        printToLogger.println();

        verify(logger).info("");
    }
}