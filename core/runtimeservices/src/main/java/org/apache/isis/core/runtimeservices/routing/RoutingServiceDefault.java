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
package org.apache.isis.core.runtimeservices.routing;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.stereotype.Controller;

import org.apache.isis.applib.services.routing.RoutingService;
import org.apache.isis.core.metamodel.services.homepage.HomePageResolverService;

import lombok.val;

@Controller
@Named("isisRuntimeServices.RoutingServiceDefault")
public class RoutingServiceDefault implements RoutingService {
    
    @Inject private HomePageResolverService homePageProviderService;

    @Override
    public boolean canRoute(final Object original) {
        return true;
    }

    @Override
    public Object route(final Object original) {
        return original != null ? original : homePageProviderService.getHomePage();
    }

    

}
