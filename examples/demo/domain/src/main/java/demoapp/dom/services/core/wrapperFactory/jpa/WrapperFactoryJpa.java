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
package demoapp.dom.services.core.wrapperFactory.jpa;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.context.annotation.Profile;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.Editing;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.Nature;
import org.apache.causeway.applib.annotation.ObjectSupport;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.factory.FactoryService;
import org.apache.causeway.applib.services.wrapper.WrapperFactory;
import org.apache.causeway.applib.services.wrapper.control.AsyncControl;
import org.apache.causeway.persistence.jpa.applib.integration.CausewayEntityListener;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.val;

import demoapp.dom.services.core.wrapperFactory.WrapperFactoryEntity;
import demoapp.dom.services.core.wrapperFactory.WrapperFactoryEntity_updatePropertyAsyncMixin;

@Profile("demo-jpa")
//tag::class[]
@Entity
@Table(
  schema = "demo",
  name = "WrapperFactoryJpa"
)
@EntityListeners(CausewayEntityListener.class)
@Named("demo.WrapperFactoryEntity")
@DomainObject(
        nature=Nature.ENTITY
        , editing = Editing.DISABLED
)
@NoArgsConstructor
public class WrapperFactoryJpa
        extends WrapperFactoryEntity {

    @Inject transient WrapperFactory wrapperFactory;
    @Inject transient FactoryService factoryService;

    // ...
//end::class[]

    public WrapperFactoryJpa(final String initialValue) {
        this.propertyAsync = initialValue;
        this.propertyAsyncMixin = initialValue;
    }

    @ObjectSupport public String title() {
        return "WrapperFactory";
    }

    @Id
    @GeneratedValue
    private Long id;

//tag::property[]
    @Property()
    @PropertyLayout(fieldSetId = "async", sequence = "1")
    @Getter @Setter
    private String propertyAsync;

    @Property()
    @PropertyLayout(fieldSetId = "async", sequence = "2")
    @Getter @Setter
    private String propertyAsyncMixin;
//end::property[]

//tag::async[]
    @Action(
        semantics = SemanticsOf.IDEMPOTENT
    )
    @ActionLayout(
        describedAs = "@Action()"
        , associateWith = "propertyAsync"
        , sequence = "1"
    )
    public WrapperFactoryJpa updatePropertyAsync(final String value) {
        val control = AsyncControl.returningVoid().withSkipRules();
        val wrapperFactoryJdo = this.wrapperFactory.asyncWrap(this, control);
        wrapperFactoryJdo.setPropertyAsync(value);
        return this;
    }
    @MemberSupport public String default0UpdatePropertyAsync() {
        return getPropertyAsync();
    }
//end::async[]

//end::class[]
    @SuppressWarnings("unused")
//tag::class[]
//tag::async[]
    @Action(
        semantics = SemanticsOf.IDEMPOTENT
    )
    @ActionLayout(
        describedAs = "Calls the 'updatePropertyAsync' (mixin) action asynchronously"
        , associateWith = "propertyAsyncMixin"
        , sequence = "1"
    )
    public WrapperFactoryJpa updatePropertyUsingAsyncWrapMixin(final String value) {
        val control = AsyncControl.returning(WrapperFactoryJpa.class).withSkipRules();
        val mixin = this.wrapperFactory.asyncWrapMixin(WrapperFactoryEntity_updatePropertyAsyncMixin.class, this, control);
        WrapperFactoryJpa act = (WrapperFactoryJpa) mixin.act(value);
        return this;
    }
    @MemberSupport public String default0UpdatePropertyUsingAsyncWrapMixin() {
        return new WrapperFactoryEntity_updatePropertyAsyncMixin(this).default0Act();
    }
//end::async[]

//tag::class[]

}
//end::class[]
