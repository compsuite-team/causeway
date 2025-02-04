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
package demoapp.dom.featured;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.MemberSupport;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.SemanticsOf;
import org.apache.causeway.applib.services.factory.FactoryService;

import lombok.RequiredArgsConstructor;
import lombok.val;

import demoapp.dom.domain.progmodel.actions.assoc.assoc.DemoItem;
import demoapp.dom.featured.customui.geocoding.GeoapifyClient;
import demoapp.dom.featured.customui.latlng.Zoom;
import demoapp.dom.featured.customui.vm.WhereInTheWorldVm;
import demoapp.dom.featured.layout.describedAs.DescribedAsVm;
import demoapp.dom.featured.layout.tabs.TabDemo;

@Named("demo.FeaturedMenu")
@DomainService(
        nature=NatureOfService.VIEW
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class FeaturedMenu {

    final FactoryService factoryService;

//tag::whereInTheWorldAction[]
    @Inject
    private GeoapifyClient geoapifyClient;

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(
            cssClassFa="fa-globe",
            describedAs="Opens a Custom UI page displaying a map for the provided address"
    )
    public WhereInTheWorldVm whereInTheWorld(
            final String address,
            @Zoom final int zoom) {
        val vm = new WhereInTheWorldVm();

        val latLng = geoapifyClient.geocode(address);
        vm.setAddress(address);
        vm.setLatitude(latLng.getLatitude());
        vm.setLongitude(latLng.getLongitude());
        vm.setZoom(zoom);

        return vm;
    }
//end::whereInTheWorldAction[]
    @MemberSupport public List<String> choices0WhereInTheWorld() {
        return Arrays.asList("Malvern, UK", "Vienna, Austria", "Leeuwarden, Netherlands", "Dublin, Ireland");
    }
    @MemberSupport public String default0WhereInTheWorld() {
        return "Malvern, UK";
    }
    @MemberSupport public int default1WhereInTheWorld() {
        return 14;
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(cssClassFa="fa-comment", describedAs="Opens the Tooltip-Demo page.")
    public DescribedAsVm toolTips(){
        val demo = factoryService.viewModel(new DescribedAsVm());

        demo.getCollection().add(DemoItem.of("first"));
        demo.getCollection().add(DemoItem.of("second"));
        demo.getCollection().add(DemoItem.of("third"));

        return demo;
    }

    @Action
    @ActionLayout(
            cssClassFa="fa-bolt",
            describedAs="Opens the Tabs-Demo page."
    )
    public TabDemo tabDemo(){
        return factoryService.viewModel(new TabDemo());
    }

}
