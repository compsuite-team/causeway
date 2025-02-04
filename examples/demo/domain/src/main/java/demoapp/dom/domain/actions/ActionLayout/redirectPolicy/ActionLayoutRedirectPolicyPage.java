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
package demoapp.dom.domain.actions.ActionLayout.redirectPolicy;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainObject;
import org.apache.causeway.applib.annotation.Nature;
import org.apache.causeway.applib.annotation.ObjectSupport;
import org.apache.causeway.applib.annotation.Optionality;
import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.PropertyLayout;
import org.apache.causeway.applib.annotation.Redirect;
import org.apache.causeway.applib.annotation.Repainting;
import org.apache.causeway.applib.services.clock.ClockService;

import demoapp.dom._infra.asciidocdesc.HasAsciiDocDescription;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

@DomainObject(nature=Nature.VIEW_MODEL)
@Named("demo.ActionLayoutRedirectPolicyPage")
@XmlRootElement(name = "root")
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
//tag::class[]
// ...
public class ActionLayoutRedirectPolicyPage
//end::class[]
implements HasAsciiDocDescription
//tag::class[]
{
    @Property(optionality = Optionality.OPTIONAL)
    @XmlElement(required = false)
    @Getter @Setter
    private Integer count;

    @SneakyThrows
    @Property
    @PropertyLayout(repainting = Repainting.NO_REPAINT) // <.>
    public String getCurrentTime() {
        Thread.sleep(1000);
        return clockService.getClock().nowAsLocalDateTime().toString();
    }

    // ...
//end::class[]

    @ObjectSupport public String title() {
        return "@ActionLayout#redirectPolicy";
    }

//tag::class[]

    @Inject @XmlTransient private ClockService clockService;
}
//end::class[]
