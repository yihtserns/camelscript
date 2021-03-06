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

import com.github.yihtserns.camelscript.transform.testutil.LocalResources;
import groovy.lang.GString;
import groovy.lang.GroovyShell;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.codehaus.groovy.control.CompilationFailedException;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author yihtserns
 */
public class CamelScriptASTTransformationTest {

    public static boolean useFirstRoute;
    private LocalResources resources = LocalResources.forClass(getClass());

    @Test
    public void shouldBeAbleToBuildRoutesUsingSyntacticSugar() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("BuildRoutesSyntacticSugar.camel")), is((Object) "Result"));
    }

    @Test
    public void shouldBeAbleToAddClosureAsProcessor() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("ClosureAsProcessor.camel")), is((Object) "Result"));
    }

    @Test
    public void shouldBeAbleToAddClosureAsTransformer() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("ClosureAsTransformer.camel")), is((Object) "Result"));
    }

    @Test
    public void shouldBeAbleToAddClosureAsPredicate() throws Exception {
        List<String> results = (List) evaluateAndReturnResult(resources.getFile("ClosureAsPredicate.camel"));
        assertThat(results, contains(null, "Result"));
    }

    @Test
    public void shouldBeAbleToAddClosureAsProcessOnException() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("ClosureForOnException.camel")), is((Object) "InException"));
    }

    /**
     * @see http://camel.apache.org/configuring-camel.html#ConfiguringCamel-WorkingwithSpringXML
     */
    @Test
    public void shouldBeAbleToReferToComponentInScriptBinding() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("ComponentInScriptBinding.camel")), is((Object) "Result"));
    }

    /**
     * @see http://camel.apache.org/configuring-camel.html#ConfiguringCamel-ReferringbeansfromEndpointURIs
     */
    @Test
    public void shouldBeAbleToReferToInstanceInScriptBinding() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("InstanceInScriptBinding.camel")), is((Object) "Result"));
    }

    @Test
    public void shouldBeAbleBuildSmootherRoutesUsingSyntacticSugar() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("SmootherRoutesSyntacticSugar.camel")), is((Object) "Result"));
    }

    @Test
    public void canConvertMessageUsingAsKeyword() throws Exception {
        List<Object> results = (List) evaluateAndReturnResult(resources.getFile("ConvertMessageAs.camel"));
        assertThat(results, everyItem(is(instanceOf(Date.class))));
    }

    @Test
    public void canLoopToBuildRoute() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("LoopToBuildRoute.camel")), is((Object) "Result"));
    }

    @Test
    public void canDoConditionalCheckToBuildRoute() throws Exception {
        useFirstRoute = true;
        assertThat(evaluateAndReturnResult(resources.getFile("ConditionalCheckBuildRoute.camel")), is((Object) "Result1"));

        useFirstRoute = false;
        assertThat(evaluateAndReturnResult(resources.getFile("ConditionalCheckBuildRoute.camel")), is((Object) "Result2"));
    }

    /**
     * Provides user with an OOTB SLF4J Logger.
     */
    @Test
    public void shouldBeAbleToLogWithoutError() throws Exception {
        evaluateAndReturnResult(resources.getFile("LogWithoutError.camel"));
    }

    /**
     * Redirect print statements to logger.
     * <p/>
     * NOTE: No proper assertion.
     */
    @Test
    public void shouldBeAbleToPrintWithoutError() throws Exception {
        evaluateAndReturnResult(resources.getFile("PrintWithoutError.camel"));
    }

    @Test
    public void getGroovyStringAsJavaString() throws Exception {
        evaluateAndReturnResult(resources.getFile("GetGroovyStringAsJavaString.camel"));
    }

    @Test
    public void shouldBeAbleToConvertTypeWhenBuildingRoute() throws Exception {
        evaluateAndReturnResult(resources.getFile("ConvertTypeWhenBuildingRoute.camel"));
    }

    private Object evaluateAndReturnResult(File groovyFile) throws CompilationFailedException, IOException {
        Object result = new GroovyShell().evaluate(groovyFile);
        return (result instanceof GString) ? result.toString() : result;
    }
}
