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
package org.apache.isis.extensions.secman.api.user.dom.mixins;

import java.util.Collection;

import javax.inject.Inject;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.MemberSupport;
import org.apache.isis.applib.services.message.MessageService;
import org.apache.isis.commons.internal.base._NullSafe;
import org.apache.isis.extensions.secman.api.role.dom.ApplicationRole;
import org.apache.isis.extensions.secman.api.role.dom.ApplicationRoleRepository;
import org.apache.isis.extensions.secman.api.user.dom.ApplicationUser;
import org.apache.isis.extensions.secman.api.user.dom.ApplicationUser.RemoveRoleDomainEvent;
import org.apache.isis.extensions.secman.api.user.dom.ApplicationUserRepository;

import lombok.RequiredArgsConstructor;

@Action(
        domainEvent = RemoveRoleDomainEvent.class,
        associateWith = "roles")
@ActionLayout(named="Remove", sequence = "2")
@RequiredArgsConstructor
public class ApplicationUser_removeRoles {

    @Inject private MessageService messageService;
    @Inject private ApplicationRoleRepository applicationRoleRepository;
    @Inject private ApplicationUserRepository applicationUserRepository;

    private final ApplicationUser target;


    @MemberSupport
    public ApplicationUser act(Collection<ApplicationRole> roles) {

        _NullSafe.stream(roles)
        .filter(this::canRemove)
        .forEach(role->applicationRoleRepository.removeRoleFromUser(role, target));

        return target;
    }

    // same logic in ApplicationRole_removeUsers
    public boolean canRemove(
            final ApplicationRole applicationRole) {

        if(applicationUserRepository.isAdminUser(target)
                && applicationRoleRepository.isAdminRole(applicationRole)) {
            messageService.warnUser("Cannot remove admin user from the admin role.");
            return false;
        }
        return true;
    }


}
