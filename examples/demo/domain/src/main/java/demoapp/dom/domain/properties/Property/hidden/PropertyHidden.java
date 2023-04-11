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
package demoapp.dom.domain.properties.Property.hidden;

import demoapp.dom._infra.asciidocdesc.HasAsciiDocDescription;
import demoapp.dom._infra.values.ValueHolder;

import org.apache.causeway.applib.annotation.Property;
import org.apache.causeway.applib.annotation.Where;

//tag::class[]
public abstract class PropertyHidden
//end::class[]
        implements
        HasAsciiDocDescription,
        ValueHolder<String>
//tag::class[]
{
    // ...
//end::class[]
    public String title() {
        return value();
    }

    @Override
    public String value() {
        return getName();
    }


//tag::none[]
    @Property()                                     // <.>
    public abstract String getName();
    public abstract void setName(String value);
//end::none[]

//tag::object-forms[]
    @Property(hidden = Where.OBJECT_FORMS)          // <.>
    public abstract String getNameHiddenObjectForms();
    public abstract void setNameHiddenObjectForms(String value);
//end::object-forms[]

//tag::all-tables[]
    @Property(hidden = Where.ALL_TABLES)            // <.>
    public abstract String getNameHiddenAllTables();
    public abstract void setNameHiddenAllTables(String value);
//end::all-tables[]

//tag::everywhere[]
    @Property(hidden = Where.EVERYWHERE)            // <.>
    public abstract String getNameHiddenEverywhere();
    public abstract void setNameHiddenEverywhere(String value);
//end::everywhere[]

//tag::class[]
}
//end::class[]
