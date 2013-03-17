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

import groovy.lang.GString;
import groovy.lang.GroovyShell;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.codehaus.groovy.control.CompilationFailedException;
import org.junit.Test;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author yihtserns
 */
public class CamelScriptASTTransformationTest {

    @Test
    public void shouldBeAbleToTreatCamelScriptClassAsCamelContext() throws Exception {
        assertThat(evaluateAndReturnResult(getCamelScriptFileWithSuffix("CamelScriptAsCamelContext")), is((Object) "Result"));
    }

    /**
     * So that can use Groovy's Category.
     */
    @Test
    public void shouldMakeCamelScriptInstanceOfCamelContext() throws Exception {
        assertThat(evaluateAndReturnResult(getCamelScriptFileWithSuffix("CamelScriptIsCamelContext")), is((Object) true));
    }

    @Test
    public void shouldBeAbleToBuildRoutesUsingSyntacticSugar() throws Exception {
        assertThat(evaluateAndReturnResult(getCamelScriptFileWithSuffix("BuildRoutesSyntacticSugar")), is((Object) "Result"));
    }

    @Test
    public void shouldBeAbleToAddClosureAsProcessor() throws Exception {
        assertThat(evaluateAndReturnResult(getCamelScriptFileWithSuffix("ClosureAsProcessor")), is((Object) "Result"));
    }

    /**
     * @see http://camel.apache.org/configuring-camel.html#ConfiguringCamel-WorkingwithSpringXML
     */
    @Test
    public void shouldBeAbleToReferToComponentInScriptBinding() throws Exception {
        assertThat(evaluateAndReturnResult(getCamelScriptFileWithSuffix("ComponentInScriptBinding")), is((Object) "Result"));
    }

    /**
     * @see http://camel.apache.org/configuring-camel.html#ConfiguringCamel-ReferringbeansfromEndpointURIs
     */
    @Test
    public void shouldBeAbleToReferToInstanceInScriptBinding() throws Exception {
        assertThat(evaluateAndReturnResult(getCamelScriptFileWithSuffix("InstanceInScriptBinding")), is((Object) "Result"));
    }

    private Object evaluateAndReturnResult(File groovyFile) throws CompilationFailedException, IOException {
        Object result = new GroovyShell().evaluate(groovyFile);
        return (result instanceof GString) ? result.toString() : result;
    }

    private File getCamelScriptFileWithSuffix(String suffix) throws URISyntaxException {
        final String filename = "CamelScriptASTTransformationTest_" + suffix + ".groovy";
        return new File(getClass().getResource(filename).toURI());
    }
}