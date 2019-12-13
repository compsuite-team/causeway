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

package org.apache.isis.viewer.wicket.ui.components.collectioncontents.multiple;

import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;

import org.apache.isis.viewer.wicket.model.hints.IsisEnvelopeEvent;
import org.apache.isis.viewer.wicket.model.hints.IsisSelectorEvent;
import org.apache.isis.viewer.wicket.model.models.EntityCollectionModel;
import org.apache.isis.viewer.wicket.model.models.EntityModel;
import org.apache.isis.viewer.wicket.model.util.ComponentHintKey;
import org.apache.isis.viewer.wicket.ui.ComponentFactory;
import org.apache.isis.viewer.wicket.ui.ComponentType;
import org.apache.isis.viewer.wicket.ui.components.collection.count.CollectionCountProvider;
import org.apache.isis.viewer.wicket.ui.components.collection.selector.CollectionSelectorHelper;
import org.apache.isis.viewer.wicket.ui.components.collection.selector.CollectionSelectorPanel;
import org.apache.isis.viewer.wicket.ui.components.collection.selector.CollectionSelectorProvider;
import org.apache.isis.viewer.wicket.ui.panels.PanelAbstract;

/**
 * Subscribes to events generated by 
 * {@link org.apache.isis.viewer.wicket.ui.components.collection.selector.CollectionSelectorPanel}, 
 * rendering the appropriate {@link ComponentType#COLLECTION_CONTENTS}
 * view for a backing {@link EntityCollectionModel}.
 */
public class CollectionContentsMultipleViewsPanel
extends PanelAbstract<EntityCollectionModel> 
implements CollectionCountProvider {

    private static final long serialVersionUID = 1L;

    private static final int MAX_NUM_UNDERLYING_VIEWS = 10;

    private static final String UIHINT_VIEW = EntityCollectionModel.HINT_KEY_SELECTED_ITEM;

    private final String underlyingIdPrefix;
    private final CollectionSelectorHelper selectorHelper;

    private Component selectedComponent;

    private Component[] underlyingViews;

    public CollectionContentsMultipleViewsPanel(
            final String id,
            final EntityCollectionModel model) {
        super(id, model);

        this.underlyingIdPrefix = ComponentType.COLLECTION_CONTENTS.toString();
        final EntityModel entityModel = model.getEntityModel();

        final ComponentHintKey selectedItemSessionAttribute =
                entityModel != null
                ? ComponentHintKey.create(super.getCommonContext(), this, EntityCollectionModel.HINT_KEY_SELECTED_ITEM)
                        : null;

                selectorHelper = new CollectionSelectorHelper(
                        model, getComponentFactoryRegistry(), selectedItemSessionAttribute);
    }

    /**
     * Build UI only after added to parent.
     */
    @Override
    public void onInitialize() {
        super.onInitialize();
        addUnderlyingViews();
    }

    private void addUnderlyingViews() {
        final EntityCollectionModel model = getModel();

        final List<ComponentFactory> componentFactories = selectorHelper.getComponentFactories();

        final CollectionSelectorPanel selectorDropdownPanelIfAny =
                CollectionSelectorProvider.Util.getCollectionSelectorProvider(this);
        final String selected;
        if (selectorDropdownPanelIfAny != null) {
            selected = selectorHelper.honourViewHintElseDefault(selectorDropdownPanelIfAny);
        } else {
            selected = componentFactories.get(0).getName();
        }

        // create all, hide the one not selected
        int i = 0;
        int selectedIdx = 0;
        underlyingViews = new Component[MAX_NUM_UNDERLYING_VIEWS];
        final EntityCollectionModel emptyModel = model.asDummy();
        for (ComponentFactory componentFactory : componentFactories) {
            final String underlyingId = underlyingIdPrefix + "-" + i;

            final boolean isSelected = selected.equals(componentFactory.getName());
            final Component underlyingView = componentFactory.createComponent(underlyingId, isSelected ? model : emptyModel);
            if(isSelected) {
                selectedIdx = i;
            }
            underlyingViews[i++] = underlyingView;
            this.addOrReplace(underlyingView);
        }

        // hide any unused placeholders
        while(i<MAX_NUM_UNDERLYING_VIEWS) {
            String underlyingId = underlyingIdPrefix + "-" + i;
            permanentlyHide(underlyingId);
            i++;
        }

        this.setOutputMarkupId(true);

        for(i=0; i<MAX_NUM_UNDERLYING_VIEWS; i++) {
            Component component = underlyingViews[i];
            if(component != null) {
                if(i != selectedIdx) {
                    super.setVisible(component, /*visible*/ false);
                } else {
                    selectedComponent = component;
                }
            }
        }
    }

    @Override
    public void onEvent(IEvent<?> event) {
        super.onEvent(event);

        final IsisSelectorEvent selectorEvent = IsisEnvelopeEvent.openLetter(event, IsisSelectorEvent.class);
        if(selectorEvent == null) {
            return;
        }
        final CollectionSelectorPanel selectorDropdownPanel = CollectionSelectorProvider.Util.getCollectionSelectorProvider(this);
        if(selectorDropdownPanel == null) {
            // not expected, because this event shouldn't be called.
            // but no harm in simply returning...
            return;
        }

        String selectedView = selectorEvent.hintFor(selectorDropdownPanel, UIHINT_VIEW);
        if (selectedView == null) {
            return;
        }

        int underlyingViewNum = selectorHelper.lookup(selectedView);

        final EntityCollectionModel dummyModel = getModel().asDummy();
        for(int i=0; i<MAX_NUM_UNDERLYING_VIEWS; i++) {
            final Component component = underlyingViews[i];
            if(component == null) {
                continue;
            }
            final boolean isSelected = i == underlyingViewNum;
            setVisible(component, isSelected);
            component.setDefaultModel(isSelected? getModel(): dummyModel);
        }

        this.selectedComponent = underlyingViews[underlyingViewNum];

        final AjaxRequestTarget target = selectorEvent.getTarget();
        if(target != null) {
            target.add(this, selectorDropdownPanel);
        }
    }

    @Override
    public Integer getCount() {
        if(selectedComponent instanceof CollectionCountProvider) {
            final CollectionCountProvider collectionCountProvider = (CollectionCountProvider) selectedComponent;
            return collectionCountProvider.getCount();
        } else {
            return null;
        }
    }


}
