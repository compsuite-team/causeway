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
package demoapp.dom.domain.collections.CollectionLayout;

import javax.inject.Named;

import org.apache.causeway.applib.annotation.Action;
import org.apache.causeway.applib.annotation.ActionLayout;
import org.apache.causeway.applib.annotation.DomainService;
import org.apache.causeway.applib.annotation.NatureOfService;
import org.apache.causeway.applib.annotation.PriorityPrecedence;
import org.apache.causeway.applib.annotation.SemanticsOf;

import demoapp.dom.domain.collections.CollectionLayout.cssClass.CollectionLayoutCssClassPage;
import demoapp.dom.domain.collections.CollectionLayout.defaultView.CollectionLayoutDefaultViewPage;
import demoapp.dom.domain.collections.CollectionLayout.describedAs.CollectionLayoutDescribedAsPage;
import demoapp.dom.domain.collections.CollectionLayout.hidden.CollectionLayoutHiddenPage;
import demoapp.dom.domain.collections.CollectionLayout.named.CollectionLayoutNamedPage;
import demoapp.dom.domain.collections.CollectionLayout.paged.CollectionLayoutPagedPage;
import demoapp.dom.domain.collections.CollectionLayout.sequence.CollectionLayoutSequencePage;
import demoapp.dom.domain.collections.CollectionLayout.sortedBy.CollectionLayoutSortedByPage;
import demoapp.dom.domain.collections.CollectionLayout.tableDecorator.CollectionLayoutTableDecoratorPage;

@Named("demo.CollectionLayoutMenu")
@DomainService(
        nature=NatureOfService.VIEW
)
@javax.annotation.Priority(PriorityPrecedence.EARLY)
//@Log4j2
public class CollectionLayoutMenu {

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(cssClassFa="fa-pen-nib",
        describedAs = "CSS class to wrap the UI component representing this collection")
    public CollectionLayoutCssClassPage cssClass(){
        return new CollectionLayoutCssClassPage();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(cssClassFa="fa-atom",
        describedAs = "View collection as a table, or collapsed, or some other representation if available")
    public CollectionLayoutDefaultViewPage defaultView(){
        return new CollectionLayoutDefaultViewPage();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(cssClassFa="fa-comment",
        describedAs = "Description of the collection, shown as a tooltip")
    public CollectionLayoutDescribedAsPage describedAs(){
        return new CollectionLayoutDescribedAsPage();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(cssClassFa="fa-glasses",
        describedAs = "Visibility of the collection in different contexts")
    public CollectionLayoutHiddenPage hidden(){
        return new CollectionLayoutHiddenPage();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(cssClassFa="fa-signature",
        describedAs = "Custom text for the collection's label")
    public CollectionLayoutNamedPage named(){
        return new CollectionLayoutNamedPage();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(cssClassFa="fa-fast-forward",
        describedAs = "Number of domain objects per page in this collection")
    public CollectionLayoutPagedPage paged(){
        return new CollectionLayoutPagedPage();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(cssClassFa="fa-sharp fa-solid fa-sort",
        describedAs = "Order of this member relative to other members in the same (layout) group.")
    public CollectionLayoutSequencePage sequence(){
        return new CollectionLayoutSequencePage();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(cssClassFa="fa-solid fa-arrow-down-a-z",
        describedAs = "Sort domain objects in this collection, overriding their default comparator")
    public CollectionLayoutSortedByPage sortedBy(){
        return new CollectionLayoutSortedByPage();
    }

    @Action(semantics = SemanticsOf.SAFE)
    @ActionLayout(cssClassFa="fa-solid fa-table-columns",
        describedAs = "Allows to specify a custom client side table renderer.")
    public CollectionLayoutTableDecoratorPage tableDecorator(){
        return new CollectionLayoutTableDecoratorPage();
    }

}
