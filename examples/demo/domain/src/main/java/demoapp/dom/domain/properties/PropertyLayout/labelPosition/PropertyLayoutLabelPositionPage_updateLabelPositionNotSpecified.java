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
package demoapp.dom.domain.properties.PropertyLayout.labelPosition;

import lombok.RequiredArgsConstructor;

import org.apache.causeway.applib.annotation.*;

@Action(semantics = SemanticsOf.IDEMPOTENT)
@ActionLayout(associateWith = "propertyLabelPositionNotSpecified", sequence = "1")
@RequiredArgsConstructor
public class PropertyLayoutLabelPositionPage_updateLabelPositionNotSpecified {

    private final PropertyLayoutLabelPositionPage page;

//tag::annotation[]
    @MemberSupport public PropertyLayoutLabelPositionPage act(
            @Parameter(optionality = Optionality.OPTIONAL)
            @ParameterLayout(
                    labelPosition = LabelPosition.NOT_SPECIFIED
            )
            final String newValue) {
        page.setPropertyLabelPositionNotSpecified(newValue);
        return page;
    }
//end::annotation[]
    @MemberSupport public String default0Act() {
        return page.getPropertyLabelPositionNotSpecified();
    }

}
