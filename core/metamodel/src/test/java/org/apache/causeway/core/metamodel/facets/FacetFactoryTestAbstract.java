/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.apache.causeway.core.metamodel.facets;

import java.lang.reflect.Method;
import java.util.Optional;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.causeway.applib.Identifier;
import org.apache.causeway.applib.annotation.Introspection.IntrospectionPolicy;
import org.apache.causeway.applib.id.LogicalType;
import org.apache.causeway.applib.services.i18n.TranslationService;
import org.apache.causeway.applib.services.iactnlayer.InteractionService;
import org.apache.causeway.commons.internal.assertions._Assert;
import org.apache.causeway.commons.internal.reflection._MethodFacades;
import org.apache.causeway.core.metamodel._testing.MetaModelContext_forTesting;
import org.apache.causeway.core.metamodel._testing.MethodRemover_forTesting;
import org.apache.causeway.core.metamodel.context.HasMetaModelContext;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.execution.MemberExecutorService;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facets.FacetFactory.ProcessClassContext;
import org.apache.causeway.core.metamodel.facets.FacetFactory.ProcessMethodContext;
import org.apache.causeway.core.metamodel.facets.FacetFactory.ProcessParameterContext;
import org.apache.causeway.core.metamodel.spec.ObjectSpecification;
import org.apache.causeway.core.metamodel.specloader.specimpl.ObjectActionMixedIn;
import org.apache.causeway.core.metamodel.specloader.specimpl.OneToManyAssociationMixedIn;
import org.apache.causeway.core.metamodel.specloader.specimpl.OneToOneAssociationMixedIn;
import org.apache.causeway.core.metamodel.valuesemantics.IntValueSemantics;
import org.apache.causeway.core.security.authentication.InteractionContextFactory;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.val;
import lombok.experimental.Accessors;

public abstract class FacetFactoryTestAbstract
implements HasMetaModelContext {

    // -- SCENARIO BUILDER

    @lombok.Value @Builder
    @Getter @Accessors(fluent=true)
    public static class ActionScenario {
        final Class<?> declaringClass;
        final String actionName;
        @Builder.Default
        final Optional<Class<?>> mixinClass = Optional.empty();
        public static ActionScenarioBuilder builder(final Class<?> declaringClass, final String actionName) {
            return new ActionScenario.ActionScenarioBuilder()
                    .declaringClass(declaringClass)
                    .actionName(actionName);
        }
    }

    @lombok.Value @Builder
    @Getter @Accessors(fluent=true)
    public static class ParameterScenario {
        final Class<?> declaringClass;
        final String actionName;
        final int paramIndex;
        @Builder.Default
        final Optional<Class<?>> mixinClass = Optional.empty();
        public static ParameterScenarioBuilder builder(final Class<?> declaringClass, final String actionName, final int paramIndex) {
            return new ParameterScenario.ParameterScenarioBuilder()
                    .declaringClass(declaringClass)
                    .actionName(actionName)
                    .paramIndex(paramIndex);
        }
    }

    @lombok.Value @Builder
    @Getter @Accessors(fluent=true)
    public static class PropertyScenario {
        final Class<?> declaringClass;
        final String propertyName;
        @Builder.Default
        final Optional<Class<?>> mixinClass = Optional.empty();
        public static PropertyScenarioBuilder builder(final Class<?> declaringClass, final String propertyName) {
            return new PropertyScenario.PropertyScenarioBuilder()
                    .declaringClass(declaringClass)
                    .propertyName(propertyName);
        }
    }

    @lombok.Value @Builder
    @Getter @Accessors(fluent=true)
    public static class CollectionScenario {
        final Class<?> declaringClass;
        final String collectionName;
        @Builder.Default
        final Optional<Class<?>> mixinClass = Optional.empty();
        public static CollectionScenarioBuilder builder(final Class<?> declaringClass, final String collectionName) {
            return new CollectionScenario.CollectionScenarioBuilder()
                    .declaringClass(declaringClass)
                    .collectionName(collectionName);
        }
    }

    // --

    @Getter(onMethod_ = {@Override})
    private MetaModelContext metaModelContext;


    private MethodRemover_forTesting methodRemover;

    /**
     * Override, if a custom {@link MetaModelContext_forTesting} is required for certain tests.
     */
    protected MetaModelContext_forTesting setUpMmc(
            final MetaModelContext_forTesting.MetaModelContext_forTestingBuilder builder) {
        return builder.build();
    }

    @BeforeEach
    protected void setUpAll() {

        val mockTranslationService = Mockito.mock(TranslationService.class);
        val mockInteractionService = Mockito.mock(InteractionService.class);
        val mockMemberExecutorService = Mockito.mock(MemberExecutorService.class);

        val iaContext = InteractionContextFactory.testing();

        methodRemover = new MethodRemover_forTesting();

        metaModelContext = setUpMmc(MetaModelContext_forTesting.builder()
                .translationService(mockTranslationService)
                .interactionService(mockInteractionService)
                .memberExecutor(mockMemberExecutorService)
                .valueSemantic(new IntValueSemantics()));

        Mockito.when(mockInteractionService.currentInteractionContext()).thenReturn(Optional.of(iaContext));
    }

    @AfterEach
    protected void tearDownAll() {
        methodRemover = null;
    }

    @FunctionalInterface
    public static interface MemberScenarioConsumer {
        void accept(
                ProcessMethodContext processMethodContext,
                FacetHolder facetHolder,
                FacetedMethod facetedMethod);
    }
    @FunctionalInterface
    protected static interface MixedInActionScenarioConsumer {
        void accept(
                ProcessMethodContext processMethodContext,
                ObjectSpecification mixeeSpec,
                FacetedMethod facetedMethod,
                ObjectActionMixedIn mixedInAct);
    }
    @FunctionalInterface
    protected static interface MixedInPropertyScenarioConsumer {
        void accept(
                ProcessMethodContext processMethodContext,
                ObjectSpecification mixeeSpec,
                FacetedMethod facetedMethod,
                OneToOneAssociationMixedIn mixedInProp);
    }
    @FunctionalInterface
    protected static interface MixedInCollectionScenarioConsumer {
        void accept(
                ProcessMethodContext processMethodContext,
                ObjectSpecification mixeeSpec,
                FacetedMethod facetedMethod,
                OneToManyAssociationMixedIn mixedInColl);
    }

    @FunctionalInterface
    protected static interface ParameterScenarioConsumer {
        void accept(
                ProcessParameterContext processParameterContext,
                FacetHolder facetHolder,
                FacetedMethod facetedMethod,
                FacetedMethodParameter facetedMethodParameter);
    }

    /**
     * Action scenario.
     */
    protected void actionScenario(
            final Class<?> declaringClass, final String actionName, final MemberScenarioConsumer consumer) {
        actionScenario(ActionScenario.builder(declaringClass, actionName).build(), consumer);
    }
    /**
     * Custom Action scenario.
     */
    protected void actionScenario(
            final ActionScenario scenario, final MemberScenarioConsumer consumer) {

        val declaringClass = scenario.declaringClass();
        val memberId = scenario.actionName();
        val actionMethod = _Utils.findMethodByNameOrFail(declaringClass, memberId);
        val paramTypes = actionMethod.getParameterTypes();
        val facetHolder = actionFacetHolder(declaringClass, memberId, paramTypes);
        val facetedMethod = FacetedMethod.createForAction(getMetaModelContext(), declaringClass, memberId, paramTypes);
        val processMethodContext = ProcessMethodContext
                .forTesting(declaringClass, FeatureType.ACTION, actionMethod, methodRemover, facetedMethod);

        consumer.accept(processMethodContext, facetHolder, facetedMethod);
    }
    /**
     * MixedIn Action scenario.
     */
    protected void actionScenarioMixedIn(
            final Class<?> mixeeClass, final Class<?> mixinClass, final MixedInActionScenarioConsumer consumer) {
        val scenario = ActionScenario.builder(mixeeClass, "unused")
                .mixinClass(Optional.of(mixinClass))
                .build();
        actionScenarioMixedIn(scenario, consumer);
    }
    protected void actionScenarioMixedIn(
            final ActionScenario scenario, final MixedInActionScenarioConsumer consumer) {

        val declaringClass = scenario.declaringClass();

        // get mixin main, assuming 'act'
        val mixinClass = scenario.mixinClass().orElseThrow();
        val mixedInMethod = _Utils.findMethodByNameOrFail(mixinClass, "act");

        val annotatedMethod = mixedInMethod;
        val facetedMethod = FacetedMethod.createForAction(getMetaModelContext(), mixinClass,
                _MethodFacades.regular(annotatedMethod));

        val id = facetedMethod.getFeatureIdentifier();
        assertNotNull(id.getClassName());

        val processMethodContext = new ProcessMethodContext(
                mixinClass, IntrospectionPolicy.ENCAPSULATION_ENABLED, FeatureType.ACTION,
                _MethodFacades.regular(annotatedMethod),
                methodRemover, facetedMethod, true);

        final ObjectSpecification mixeeSpec = getSpecificationLoader().loadSpecification(declaringClass);
        final ObjectActionMixedIn mixedInAct =
                ObjectActionMixedIn.forTesting.forMixinMain(mixeeSpec, mixinClass, "act", facetedMethod);

        consumer.accept(processMethodContext, mixeeSpec, facetedMethod, mixedInAct);
    }

    /**
     * Parameter scenario.
     */
    protected void parameterScenario(
            final Class<?> declaringClass, final String actionName, final int paramIndex, final ParameterScenarioConsumer consumer) {
        parameterScenario(ParameterScenario.builder(declaringClass, actionName, paramIndex).build(), consumer);
    }
    /**
     * Custom Parameter scenario.
     */
    protected void parameterScenario(
            final ParameterScenario scenario, final ParameterScenarioConsumer consumer) {
        _Assert.assertEquals(0, scenario.paramIndex(), ()->"not yet implemented otherwise");

        val declaringClass = scenario.declaringClass();
        val memberId = scenario.actionName();
        val actionMethod = _Utils.findMethodByNameOrFail(declaringClass, memberId);
        val paramTypes = actionMethod.getParameterTypes();
        val facetHolder = actionFacetHolder(declaringClass, memberId, paramTypes);
        val facetedMethod = FacetedMethod.createForAction(getMetaModelContext(), declaringClass, memberId, paramTypes);
        val facetedMethodParameter =
                actionMethod.getParameterCount()==0
                ? (FacetedMethodParameter)null
                : new FacetedMethodParameter(getMetaModelContext(),
                    FeatureType.ACTION_PARAMETER_SINGULAR, facetedMethod.getOwningType(),
                    facetedMethod.getMethod(), 0);

        val processParameterContext =
                FacetFactory.ProcessParameterContext.forTesting(
                        declaringClass, IntrospectionPolicy.ANNOTATION_OPTIONAL, actionMethod, null, facetedMethodParameter);

        consumer.accept(processParameterContext, facetHolder, facetedMethod, facetedMethodParameter);
    }

    /**
     * Property scenario.
     */
    protected void propertyScenario(
            final Class<?> declaringClass, final String propertyName, final MemberScenarioConsumer consumer) {
        propertyScenario(PropertyScenario.builder(declaringClass, propertyName).build(), consumer);
    }
    /**
     * Custom Property scenario.
     */
    protected void propertyScenario(
            final PropertyScenario scenario, final MemberScenarioConsumer consumer) {
        val declaringClass = scenario.declaringClass();
        val memberId = scenario.propertyName();

        val facetHolder = propertyFacetHolder(declaringClass, memberId);
        val annotatedMethod = _Utils.findGetterOrFail(declaringClass, memberId);
        val facetedMethod = FacetedMethod.createForProperty(getMetaModelContext(), declaringClass, annotatedMethod);

        val processMethodContext = ProcessMethodContext
                .forTesting(declaringClass, FeatureType.PROPERTY, annotatedMethod, methodRemover, facetedMethod);

        consumer.accept(processMethodContext, facetHolder, facetedMethod);
    }
    /**
     * MixedIn Property scenario.
     */
    protected void propertyScenarioMixedIn(
            final Class<?> mixeeClass, final Class<?> mixinClass, final MixedInPropertyScenarioConsumer consumer) {
        val scenario = PropertyScenario.builder(mixeeClass, "unused")
                .mixinClass(Optional.of(mixinClass))
                .build();
        propertyScenarioMixedIn(scenario, consumer);
    }
    protected void propertyScenarioMixedIn(
            final PropertyScenario scenario, final MixedInPropertyScenarioConsumer consumer) {

        val declaringClass = scenario.declaringClass();

        // get mixin main, assuming 'prop'
        val mixinClass = scenario.mixinClass().orElseThrow();
        val annotatedMethod = _Utils.findMethodByNameOrFail(mixinClass, "prop");

        val facetedMethod = FacetedMethod.createForProperty(getMetaModelContext(), mixinClass, annotatedMethod);

        val id = facetedMethod.getFeatureIdentifier();
        assertNotNull(id.getClassName());

        val processMethodContext = new ProcessMethodContext(
                mixinClass, IntrospectionPolicy.ENCAPSULATION_ENABLED, FeatureType.PROPERTY,
                _MethodFacades.regular(annotatedMethod),
                methodRemover, facetedMethod, true);

        final ObjectSpecification mixeeSpec = getSpecificationLoader().loadSpecification(declaringClass);
        final OneToOneAssociationMixedIn mixedInProp =
                OneToOneAssociationMixedIn.forTesting.forMixinMain(mixeeSpec, mixinClass, "prop", facetedMethod);

        consumer.accept(processMethodContext, mixeeSpec, facetedMethod, mixedInProp);
    }

    /**
     * Collection scenario.
     */
    protected void collectionScenario(
            final Class<?> declaringClass, final String collectionName, final MemberScenarioConsumer consumer) {
        collectionScenario(CollectionScenario.builder(declaringClass, collectionName).build(), consumer);
    }
    /**
     * Custom Collection scenario.
     */
    protected void collectionScenario(
            final CollectionScenario scenario, final MemberScenarioConsumer consumer) {

        val declaringClass = scenario.declaringClass();
        val memberId = scenario.collectionName();
        val facetHolder = collectionFacetHolder(declaringClass, memberId);
        val annotatedMethod = _Utils.findGetterOrFail(declaringClass, memberId);
        val facetedMethod = FacetedMethod.createForProperty(getMetaModelContext(), declaringClass, annotatedMethod);

        val processMethodContext = ProcessMethodContext
                .forTesting(declaringClass, FeatureType.COLLECTION, annotatedMethod, methodRemover, facetedMethod);

        consumer.accept(processMethodContext, facetHolder, facetedMethod);
    }
    /**
     * MixedIn Collection scenario.
     */
    protected void collectionScenarioMixedIn(
            final Class<?> mixeeClass, final Class<?> mixinClass, final MixedInCollectionScenarioConsumer consumer) {
        val scenario = CollectionScenario.builder(mixeeClass, "unused")
                .mixinClass(Optional.of(mixinClass))
                .build();
        collectionScenarioMixedIn(scenario, consumer);
    }
    protected void collectionScenarioMixedIn(
            final CollectionScenario scenario, final MixedInCollectionScenarioConsumer consumer) {

        val declaringClass = scenario.declaringClass();

        // get mixin main, assuming 'coll'
        val mixinClass = scenario.mixinClass().orElseThrow();
        val annotatedMethod = _Utils.findMethodByNameOrFail(mixinClass, "coll");

        val facetedMethod = FacetedMethod.createForCollection(getMetaModelContext(), mixinClass, annotatedMethod);

        val id = facetedMethod.getFeatureIdentifier();
        assertNotNull(id.getClassName());

        val processMethodContext = new ProcessMethodContext(
                mixinClass, IntrospectionPolicy.ENCAPSULATION_ENABLED, FeatureType.COLLECTION,
                _MethodFacades.regular(annotatedMethod),
                methodRemover, facetedMethod, true);

        final ObjectSpecification mixeeSpec = getSpecificationLoader().loadSpecification(declaringClass);
        final OneToManyAssociationMixedIn mixedInColl =
                OneToManyAssociationMixedIn.forTesting.forMixinMain(mixeeSpec, mixinClass, "coll", facetedMethod);

        consumer.accept(processMethodContext, mixeeSpec, facetedMethod, mixedInColl);
    }

    /**
     * DomainObject scenario.
     */
    protected void objectScenario(final Class<?> declaringClass, final BiConsumer<ProcessClassContext, FacetHolder> consumer) {
        val facetHolder = FacetHolder.simple(getMetaModelContext(),
                Identifier.classIdentifier(LogicalType.fqcn(declaringClass)));
        val processClassContext = ProcessClassContext
                .forTesting(declaringClass, methodRemover, facetHolder);
        consumer.accept(processClassContext, facetHolder);
    }

    // -- UTILITY

    protected static Method findMethodExactOrFail(final Class<?> type, final String methodName, final Class<?>[] methodTypes) {
        return _Utils.findMethodExactOrFail(type, methodName, methodTypes);
    }

    protected static Method findMethodExactOrFail(final Class<?> type, final String methodName) {
        return _Utils.findMethodExactOrFail(type, methodName);
    }

    protected static Optional<Method> findMethodExact(final Class<?> type, final String methodName) {
        return _Utils.findMethodExact(type, methodName);
    }

    private FacetHolder actionFacetHolder(final Class<?> declaringClass, final String memberId, final Class<?>[] paramTypes) {
        return FacetHolder.simple(getMetaModelContext(),
                Identifier.actionIdentifier(LogicalType.fqcn(declaringClass), memberId, paramTypes));
    }

    private FacetHolder propertyFacetHolder(final Class<?> declaringClass, final String memberId) {
        return FacetHolder.simple(getMetaModelContext(),
                Identifier.propertyIdentifier(LogicalType.fqcn(declaringClass), memberId));
    }

    private FacetHolder collectionFacetHolder(final Class<?> declaringClass, final String memberId) {
        return FacetHolder.simple(getMetaModelContext(),
                Identifier.collectionIdentifier(LogicalType.fqcn(declaringClass), memberId));
    }

    // -- EXPECTATIONS

    protected final void assertNoMethodsRemoved() {
        assertTrue(methodRemover.getRemovedMethodMethodCalls().isEmpty());
        assertTrue(methodRemover.getRemoveMethodArgsCalls().isEmpty());
    }

    protected final void assertMethodWasRemoved(final Method method) {
        assertTrue(methodRemover.getRemovedMethodMethodCalls().contains(method),
                ()->String.format("method was not removed in test scenario: %s", method));
    }

    protected final void assertMethodWasRemoved(final Class<?> type, final String methodName) {
        assertMethodWasRemoved(findMethodExactOrFail(type, methodName));
    }

    protected final void assertMethodEqualsFirstIn(
            final @NonNull Method method,
            final @NonNull ImperativeFacet imperativeFacet) {
        _Utils.assertMethodEquals(method, imperativeFacet.getMethods().getFirstElseFail().asMethodElseFail());
    }

}
