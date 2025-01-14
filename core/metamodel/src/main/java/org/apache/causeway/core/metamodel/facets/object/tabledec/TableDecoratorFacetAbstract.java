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
package org.apache.causeway.core.metamodel.facets.object.tabledec;

import org.apache.causeway.applib.annotation.TableDecorator;
import org.apache.causeway.core.metamodel.facetapi.Facet;
import org.apache.causeway.core.metamodel.facetapi.FacetHolder;
import org.apache.causeway.core.metamodel.facets.SingleValueFacetAbstract;

import java.util.function.BiConsumer;

public abstract class TableDecoratorFacetAbstract
extends SingleValueFacetAbstract<Class<? extends TableDecorator>>
implements TableDecoratorFacet {

    public static final Class<TableDecoratorFacet> type() {
        return TableDecoratorFacet.class;
    }


    protected TableDecoratorFacetAbstract(
            final Class<? extends TableDecorator> value,
            final FacetHolder holder,
            final Facet.Precedence precedence) {
        super(type(), value, holder, precedence);
    }

    protected TableDecoratorFacetAbstract(
            final Class<? extends TableDecorator> value,
            final FacetHolder holder) {
        super(type(), value, holder);
    }

    @Override
    public void visitAttributes(final BiConsumer<String, Object> visitor) {
        super.visitAttributes(visitor);
        visitor.accept("value", value());
    }

}
