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
import groovy.lang.MissingMethodException;
import java.io.File;
import java.io.IOException;
import org.codehaus.groovy.control.CompilationFailedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.hamcrest.Matchers.*;

/**
 * @author yihtserns
 */
public class CamelScriptGlobalASTTransformationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private LocalResources resources = LocalResources.forClass(getClass());

    @Test
    public void shouldNotAutoTransformFileWithoutCamelAsExtension() throws Exception {
        thrown.expect(MissingMethodException.class);
        thrown.expectMessage(containsString("routes"));
        evaluateAndReturnResult(resources.getFile("NotCamelScript.groovy"));
    }

    private Object evaluateAndReturnResult(File groovyFile) throws CompilationFailedException, IOException {
        Object result = new GroovyShell().evaluate(groovyFile);
        return (result instanceof GString) ? result.toString() : result;
    }
}