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
package com.github.yihtserns.camelscript;

import org.apache.camel.StartupListener;
import org.apache.camel.TypeConverter;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.converter.DefaultTypeConverter;
import org.apache.camel.spi.TypeConverterRegistry;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author yihtserns
 */
public class StartLaterDefaultTypeConverterTest {

    @Test
    public void shouldAddItselfIntoCamelContext() throws Exception {
        DefaultCamelContext camelContext = spy(new DefaultCamelContext());
        StartLaterDefaultTypeConverter.registerInto(camelContext);

        verify(camelContext).setTypeConverter((TypeConverter) argThat(instanceOf(StartLaterDefaultTypeConverter.class)));
        verify(camelContext).setTypeConverterRegistry((TypeConverterRegistry) argThat(instanceOf(StartLaterDefaultTypeConverter.class)));
        verify(camelContext, atLeastOnce()).addService(argThat(instanceOf(StartLaterDefaultTypeConverter.class)));
    }

    @Test
    public void hopefullyDoesNotImplementStartupListener() {
        assertThat("DefaultTypeConverter changed to implement StartupListener, have to review "
                + StartLaterDefaultTypeConverter.class.getSimpleName()
                + " implementation",
                DefaultTypeConverter.class, not(typeCompatibleWith(StartupListener.class)));
    }
}
