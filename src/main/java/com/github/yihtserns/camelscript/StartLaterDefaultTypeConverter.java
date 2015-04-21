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

import org.apache.camel.CamelContext;
import org.apache.camel.StartupListener;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.converter.DefaultTypeConverter;
import org.apache.camel.spi.FactoryFinder;
import org.apache.camel.spi.Injector;
import org.apache.camel.spi.PackageScanClassResolver;

/**
 * @author yihtserns
 * @see #start()
 * @see #onCamelContextStarted(CamelContext, boolean)
 * @see #registerInto(DefaultCamelContext)
 */
public class StartLaterDefaultTypeConverter extends DefaultTypeConverter implements StartupListener {

    /**
     * Use {@link #registerInto(org.apache.camel.impl.DefaultCamelContext)} instead.
     *
     * @param resolver see
     * {@link DefaultTypeConverter#DefaultTypeConverter(PackageScanClassResolver, Injector, FactoryFinder)}
     * @param injector see
     * {@link DefaultTypeConverter#DefaultTypeConverter(PackageScanClassResolver, Injector, FactoryFinder)}
     * @param factoryFinder see
     * {@link DefaultTypeConverter#DefaultTypeConverter(PackageScanClassResolver, Injector, FactoryFinder)}
     */
    private StartLaterDefaultTypeConverter(
            final PackageScanClassResolver resolver,
            final Injector injector,
            final FactoryFinder factoryFinder) {
        super(resolver, injector, factoryFinder);
    }

    /**
     * Does nothing.
     *
     * @see #onCamelContextStarted(CamelContext, boolean)
     */
    @Override
    public void start() {
    }

    /**
     * Only start {@link DefaultTypeConverter} <em>after</em> all routes are started, as
     * {@link AutoGrabComponentResolver} might pull in additional jars that contain
     * {@link org.apache.camel.TypeConverter}s.
     *
     * @param context unused
     * @param alreadyStarted unused
     * @throws Exception if thrown by {@link DefaultTypeConverter#start()}
     * @see #start()
     */
    @Override
    public void onCamelContextStarted(final CamelContext context, final boolean alreadyStarted) throws Exception {
        super.start();
    }

    /**
     * Set an instance of {@link StartLaterDefaultTypeConverter} into the given {@link DefaultCamelContext} as the:
     * <ul>
     * <li>{@link DefaultCamelContext#setTypeConverter(org.apache.camel.TypeConverter) Type Converter}</li>
     * <li>{@link DefaultCamelContext#setTypeConverterRegistry(org.apache.camel.spi.TypeConverterRegistry)
     * Type Converter Registry}</li>
     * <li>{@link DefaultCamelContext#addService(java.lang.Object) Service}</li>
     * </ul>
     *
     * @param camelContext to register {@link StartLaterDefaultTypeConverter} into
     * @throws Exception if thrown by {@link DefaultCamelContext#addService(Object)}
     */
    public static void registerInto(DefaultCamelContext camelContext) throws Exception {
        StartLaterDefaultTypeConverter typeConverter = new StartLaterDefaultTypeConverter(
                camelContext.getPackageScanClassResolver(),
                camelContext.getInjector(),
                camelContext.getDefaultFactoryFinder());

        camelContext.setTypeConverter(typeConverter);
        camelContext.setTypeConverterRegistry(typeConverter);
        camelContext.addService(typeConverter);
    }
}
