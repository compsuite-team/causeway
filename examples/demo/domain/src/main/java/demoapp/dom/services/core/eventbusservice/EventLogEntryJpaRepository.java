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
package demoapp.dom.services.core.eventbusservice;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import org.apache.causeway.applib.services.repository.RepositoryService;

import lombok.RequiredArgsConstructor;

import demoapp.dom.services.core.eventbusservice.EventBusServiceDemoVm.UiButtonEvent;

@Profile("demo-jpa")
@Repository
@Named("demo.eventLogRepository")
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class EventLogEntryJpaRepository
implements EventLogEntryRepository<EventLogEntryJpa> {

    final RepositoryService repositoryService;

    @Override
    public List<EventLogEntryJpa> listAll(){
        return repositoryService.allInstances(EventLogEntryJpa.class);
    }

    @Override
    public void add(EventLogEntryJpa entry) {
        repositoryService.persist(entry);
    }

    @Override
    public EventLogEntryJpa newEntityFor(UiButtonEvent event) {
        return EventLogEntryJpa.of(event);
    }

}
