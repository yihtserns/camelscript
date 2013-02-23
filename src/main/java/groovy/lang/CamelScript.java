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
package groovy.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.codehaus.groovy.transform.GroovyASTTransformationClass;

/**
 * Annotate Groovy Script with this to turn it into a Camel Script.
 *
 * @author yihtserns
 * @see com.github.yihtserns.camelscript.transform.CamelScriptASTTransformation
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.PACKAGE)
@GroovyASTTransformationClass("com.github.yihtserns.camelscript.transform.CamelScriptASTTransformation")
public @interface CamelScript {
}