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
package demoapp.dom.domain.objects.DomainObject.introspection;

import java.util.List;

import javax.inject.Inject;

import org.apache.causeway.applib.annotation.Collection;
import org.apache.causeway.applib.annotation.CollectionLayout;
import org.apache.causeway.applib.annotation.MemberSupport;

import lombok.RequiredArgsConstructor;

import demoapp.dom._infra.values.ValueHolderRepository;
import demoapp.dom.domain.objects.DomainObject.introspection.annotOpt.DomainObjectIntrospectionAnnotOpt;

@Collection()
@CollectionLayout()
@RequiredArgsConstructor
public class DomainObjectIntrospectionPage_annotationOptional {

    @SuppressWarnings("unused")
    private final DomainObjectIntrospectionPage page;

    @MemberSupport
    public List<? extends DomainObjectIntrospectionAnnotOpt> coll() {
        return entities.all();
    }

    @Inject ValueHolderRepository<String, ? extends DomainObjectIntrospectionAnnotOpt> entities;

}
