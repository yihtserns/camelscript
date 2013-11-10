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
import org.codehaus.groovy.control.CompilationFailedException;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author yihtserns
 */
public class CamelScriptASTTransformationTest {

    private LocalResources resources = LocalResources.forClass(getClass());

    @Test
    public void shouldBeAbleToTreatCamelScriptClassAsCamelContext() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("CamelScriptAsCamelContext.groovy")), is((Object) "Result"));
    }

    /**
     * So that can use Groovy's Category.
     */
    @Test
    public void shouldMakeCamelScriptInstanceOfCamelContext() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("CamelScriptIsCamelContext.groovy")), is((Object) true));
    }

    @Test
    public void shouldBeAbleToBuildRoutesUsingSyntacticSugar() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("BuildRoutesSyntacticSugar.groovy")), is((Object) "Result"));
    }

    @Test
    public void shouldBeAbleToAddClosureAsProcessor() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("ClosureAsProcessor.groovy")), is((Object) "Result"));
    }

    /**
     * @see http://camel.apache.org/configuring-camel.html#ConfiguringCamel-WorkingwithSpringXML
     */
    @Test
    public void shouldBeAbleToReferToComponentInScriptBinding() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("ComponentInScriptBinding.groovy")), is((Object) "Result"));
    }

    /**
     * @see http://camel.apache.org/configuring-camel.html#ConfiguringCamel-ReferringbeansfromEndpointURIs
     */
    @Test
    public void shouldBeAbleToReferToInstanceInScriptBinding() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("InstanceInScriptBinding.groovy")), is((Object) "Result"));
    }

    @Test
    public void shouldBeAbleBuildSmootherRoutesUsingSyntacticSugar() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("SmootherRoutesSyntacticSugar.groovy")), is((Object) "Result"));
    }

    /**
     * Like referring to a prototype Spring bean.
     */
    @Test
    public void shouldBeAbleToUseClosureInScriptBindingAsFactoryMethod() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("ClosureInScriptBinding.groovy")), is((Object) "Result"));
    }

    @Test
    public void canConvertMessageUsingAsKeyword() throws Exception {
        assertThat(evaluateAndReturnResult(resources.getFile("ConvertMessageAs.groovy")), is(instanceOf(Date.class)));
    }

    /**
     * Provides user with an OOTB SLF4J Logger.
     */
    @Test
    public void shouldBeAbleToLogWithoutError() throws Exception {
        evaluateAndReturnResult(resources.getFile("LogWithoutError.groovy"));
    }

    /**
     * Redirect print statements to logger.
     */
    @Test
    public void shouldBeAbleToPrintWithoutError() throws Exception {
        evaluateAndReturnResult(resources.getFile("PrintWithoutError.groovy"));
    }

    private Object evaluateAndReturnResult(File groovyFile) throws CompilationFailedException, IOException {
        Object result = new GroovyShell().evaluate(groovyFile);
        return (result instanceof GString) ? result.toString() : result;
    }
}