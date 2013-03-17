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
package com.github.yihtserns.camelscript.transform.testutil;

import com.google.common.io.Files;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author yihtserns
 */
public class LocalResourcesTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    private LocalResources resources = LocalResources.forClass(getClass());

    @Test
    public void shouldThrowFileNotFoundExceptionWhenFileDoesNotExists() throws Exception {
        thrown.expect(FileNotFoundException.class);
        thrown.expectMessage(endsWith("LocalResourcesTest_DoesNotExists.txt"));
        resources.getFile("DoesNotExists.txt");
    }

    @Test
    public void shouldBeAbleToFindLocalResource() throws Exception {
        File file = resources.getFile("ShouldExists.txt");
        assertThat(Files.toString(file, Charset.defaultCharset()), is("You Found Me!"));
    }
}