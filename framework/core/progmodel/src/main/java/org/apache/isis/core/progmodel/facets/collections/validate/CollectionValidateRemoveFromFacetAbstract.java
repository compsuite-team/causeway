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

package org.apache.isis.core.progmodel.facets.collections.validate;

import org.apache.isis.applib.events.ValidityEvent;
import org.apache.isis.core.metamodel.facetapi.Facet;
import org.apache.isis.core.metamodel.facetapi.FacetAbstract;
import org.apache.isis.core.metamodel.facetapi.FacetHolder;
import org.apache.isis.core.metamodel.interactions.CollectionRemoveFromContext;
import org.apache.isis.core.metamodel.interactions.ValidityContext;

public abstract class CollectionValidateRemoveFromFacetAbstract extends FacetAbstract implements CollectionValidateRemoveFromFacet {

    public static Class<? extends Facet> type() {
        return CollectionValidateRemoveFromFacet.class;
    }

    public CollectionValidateRemoveFromFacetAbstract(final FacetHolder holder) {
        super(type(), holder, false);
    }

    @Override
    public String invalidates(final ValidityContext<? extends ValidityEvent> context) {
        if (!(context instanceof CollectionRemoveFromContext)) {
            return null;
        }
        final CollectionRemoveFromContext collectionRemoveFromContext = (CollectionRemoveFromContext) context;
        return invalidReason(context.getTarget(), collectionRemoveFromContext.getProposed());
    }
}
