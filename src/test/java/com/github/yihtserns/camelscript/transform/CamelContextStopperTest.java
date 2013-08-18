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

import org.apache.camel.CamelContext;
import org.junit.Test;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author yihtserns
 */
public class CamelContextStopperTest {

    private CamelContext context = mock(CamelContext.class);

    @Test
    public void shouldStopCamelContextWhenRan() throws Exception {
        CamelContextStopper contextStopper = new CamelContextStopper(context);
        contextStopper.run();

        verify(context).stop();
    }

    @Test
    public void shouldRethrowExceptionThrownByCamelContext() throws Exception {
        Throwable simulateEx = new Exception("Simulated");
        CamelContextStopper contextStopper = new CamelContextStopper(context);

        doThrow(simulateEx).when(context).stop();

        try {
            contextStopper.run();
            fail("Should throw " + simulateEx);
        } catch (RuntimeException ex) {
            assertThat(ex.getCause(), is(simulateEx));
        }
    }
}