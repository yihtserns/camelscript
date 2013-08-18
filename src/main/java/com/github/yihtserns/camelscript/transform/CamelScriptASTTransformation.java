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

import com.github.yihtserns.camelscript.CamelContextCategory;
import groovy.lang.Delegate;
import groovy.lang.Mixin;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.ast.MixinASTTransformation;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.ConstructorCallExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.DelegateASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.objectweb.asm.Opcodes;
import static org.codehaus.groovy.ast.expr.VariableExpression.THIS_EXPRESSION;

/**
 * Transformer to turn a Groovy Script into a Camel Script.
 *
 * @author yihtserns
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class CamelScriptASTTransformation implements ASTTransformation {

    private static final String CAMEL_CONTEXT_FIELD_NAME = "camelContext";
    private DelegateASTTransformation delegateTransformation = new DelegateASTTransformation();
    private MixinASTTransformation mixinTransformation = new MixinASTTransformation();

    /**
     * Source code representation of what this method is doing:
     * <pre>
     * {@literal @}Mixin({@link CamelContextCategory})
     * public class SCRIPT_NAME {
     *      {@literal @}Delegate
     *      CamelContext camelContext = new DefaultCamelContext(new {@link ScriptBindingRegistry}(this));
     * }
     * </pre>
     */
    public void visit(final ASTNode[] nodes, final SourceUnit source) {
        final ClassNode scriptClassNode = source.getAST().getScriptClassDummy();
        ScriptClassNodeTransformer transformer = new ScriptClassNodeTransformer(scriptClassNode, source);

        if (scriptClassNode.getField(CAMEL_CONTEXT_FIELD_NAME) != null) {
            // Encountered inner class
            return;
        }

        Expression newScriptRegistry = constructorOf(ScriptBindingRegistry.class, THIS_EXPRESSION);
        Expression newCamelContext = constructorOf(DefaultCamelContext.class, newScriptRegistry);

        transformer.delegateTo(fieldNode(CAMEL_CONTEXT_FIELD_NAME, CamelContext.class, newCamelContext));
        transformer.mixin(CamelContextCategory.class);
    }

    private Expression constructorOf(final Class clazz, final Expression constructorArg) {
        return new ConstructorCallExpression(new ClassNode(clazz), constructorArg);
    }

    /**
     * Convenience method to create {@link FieldNode}.
     * @param type field type
     * @param initialValueExpression initial value for the field
     * @return
     */
    private FieldNode fieldNode(
            final String fieldName, final Class<CamelContext> type, final Expression initialValueExpression) {
        return new FieldNode(fieldName, Opcodes.ACC_PRIVATE, new ClassNode(type), null, initialValueExpression);
    }

    private class ScriptClassNodeTransformer {

        private ClassNode scriptClassNode;
        private SourceUnit source;

        public ScriptClassNodeTransformer(final ClassNode scriptClassNode, final SourceUnit source) {
            this.scriptClassNode = scriptClassNode;
            this.source = source;
        }

        /**
         * @param fieldNode delegate for the Groovy Script
         * @see {@link Delegate}
         */
        public void delegateTo(final FieldNode fieldNode) {
            AnnotationNode delegateAnnotationNode = new AnnotationNode(new ClassNode(Delegate.class));
            delegateAnnotationNode.setMember("deprecated", new ConstantExpression(true));

            scriptClassNode.addField(fieldNode);
            delegateTransformation.visit(
                    new ASTNode[]{delegateAnnotationNode, fieldNode},
                    source);
        }

        /**
         * @param categoryClass Groovy Category to be mixed into the Groovy Script
         */
        public void mixin(final Class categoryClass) {
            AnnotationNode categoryAnnotationNode = new AnnotationNode(new ClassNode(Mixin.class));
            categoryAnnotationNode.setMember("value", new ClassExpression(new ClassNode(categoryClass)));

            mixinTransformation.visit(
                    new ASTNode[]{categoryAnnotationNode, scriptClassNode},
                    source);
        }
    }
}