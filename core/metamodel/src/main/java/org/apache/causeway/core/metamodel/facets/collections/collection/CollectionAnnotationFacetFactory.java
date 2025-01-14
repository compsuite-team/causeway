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
package org.apache.causeway.core.metamodel.facets.collections.collection;

import java.util.Optional;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.core.config.progmodel.ProgrammingModelConstants;
import org.apache.causeway.core.metamodel.context.MetaModelContext;
import org.apache.causeway.core.metamodel.facetapi.FeatureType;
import org.apache.causeway.core.metamodel.facets.FacetFactoryAbstract;
import org.apache.causeway.core.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.apache.causeway.core.metamodel.facets.actions.contributing.ContributingFacet.Contributing;
import org.apache.causeway.core.metamodel.facets.actions.contributing.ContributingFacetAbstract;
import org.apache.causeway.core.metamodel.facets.actions.semantics.ActionSemanticsFacetAbstract;
import org.apache.causeway.core.metamodel.facets.collections.collection.hidden.HiddenFacetForCollectionAnnotation;
import org.apache.causeway.core.metamodel.facets.collections.collection.modify.CollectionDomainEventFacet;
import org.apache.causeway.core.metamodel.facets.collections.collection.typeof.TypeOfFacetForCollectionAnnotation;
import org.apache.causeway.core.metamodel.facets.propcoll.accessor.PropertyOrCollectionAccessorFacet;
import org.apache.causeway.core.metamodel.specloader.validator.MetaModelValidatorForAmbiguousMixinAnnotations;

import lombok.val;

public class CollectionAnnotationFacetFactory
extends FacetFactoryAbstract {

    @Inject
    public CollectionAnnotationFacetFactory(final MetaModelContext mmc) {
        super(mmc, FeatureType.COLLECTIONS_AND_ACTIONS);
    }

    @Override
    public void process(final ProcessMethodContext processMethodContext) {

        val collectionIfAny = collectionIfAny(processMethodContext);

        inferIntentWhenOnTypeLevel(processMethodContext, collectionIfAny);

        processDomainEvent(processMethodContext, collectionIfAny);
        processHidden(processMethodContext, collectionIfAny);
        processTypeOf(processMethodContext, collectionIfAny);
    }

    Optional<Collection> collectionIfAny(final ProcessMethodContext processMethodContext) {
        return processMethodContext
            .synthesizeOnMethodOrMixinType(
                    Collection.class,
                    () -> MetaModelValidatorForAmbiguousMixinAnnotations
                    .addValidationFailure(processMethodContext.getFacetHolder(), Collection.class));
    }

    void inferIntentWhenOnTypeLevel(final ProcessMethodContext processMethodContext, final Optional<Collection> collectionIfAny) {
        if(!processMethodContext.isMixinMain() || !collectionIfAny.isPresent()) {
            return; // no @Collection found neither type nor method
        }

        //          XXX[1998] this condition would allow 'intent inference' only when @Property is found at type level
        //          val isPropertyMethodLevel = processMethodContext.synthesizeOnMethod(Property.class).isPresent();
        //          if(isPropertyMethodLevel) return;

        //[1998] if @Collection detected on method or type level infer:
        //@Action(semantics=SAFE)
        //@ActionLayout(contributed=ASSOCIATION) ... it seems, is already allowed for mixins
        val facetedMethod = processMethodContext.getFacetHolder();
        addFacet(new ActionSemanticsFacetAbstract(SemanticsOf.SAFE, facetedMethod) {});
        addFacet(new ContributingFacetAbstract(Contributing.AS_ASSOCIATION, facetedMethod) {});

    }

    void processDomainEvent(final ProcessMethodContext processMethodContext, final Optional<Collection> collectionIfAny) {

        val cls = processMethodContext.getCls();
        val holder = processMethodContext.getFacetHolder();

        val getterFacetIfAny = holder.lookupFacet(PropertyOrCollectionAccessorFacet.class);

        final boolean isCollection = getterFacetIfAny.isPresent()
                || (processMethodContext.isMixinMain()
                        && collectionIfAny.isPresent());

        if(!isCollection) return; // bale out if method is not representing a collection (no matter mixed-in or not)

        //
        // Set up CollectionDomainEventFacet, which will act as the hiding/disabling/validating advisor
        //

        // search for @Collection(domainEvent=...)
        addFacet(
            CollectionDomainEventFacet
                .create(collectionIfAny, cls, holder));
    }

    @SuppressWarnings("removal")
    void processHidden(final ProcessMethodContext processMethodContext, final Optional<Collection> collectionIfAny) {
        val holder = processMethodContext.getFacetHolder();

        // check for @Collection(hidden=...)
        addFacetIfPresent(
                HiddenFacetForCollectionAnnotation
                .create(collectionIfAny, holder));
    }


    void processTypeOf(final ProcessMethodContext processMethodContext, final Optional<Collection> collectionIfAny) {

        val cls = processMethodContext.getCls();
        val facetHolder = processMethodContext.getFacetHolder();
        val method = processMethodContext.getMethod();

        val methodReturnType = method.getReturnType();
        ProgrammingModelConstants.CollectionSemantics.valueOf(methodReturnType)
        .ifPresent(collectionType->{
            addFacetIfPresent(
                    // check for @Collection(typeOf=...)
                    TypeOfFacetForCollectionAnnotation
                    .create(collectionIfAny, collectionType, facetHolder)
                    .or(
                        // else infer from return type
                        ()-> TypeOfFacet.inferFromMethodReturnType(
                                cls,
                                method,
                                facetHolder))
                );

        });
    }

}
