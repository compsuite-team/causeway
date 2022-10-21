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
package org.apache.causeway.extensions.secman.applib.role.seed;

import org.apache.causeway.commons.collections.Can;
import org.apache.causeway.extensions.secman.applib.permission.dom.ApplicationPermissionMode;
import org.apache.causeway.extensions.secman.applib.permission.dom.ApplicationPermissionRule;
import org.apache.causeway.extensions.secman.applib.role.fixtures.AbstractRoleAndPermissionsFixtureScript;
import org.apache.causeway.applib.CausewayModuleApplib;
import org.apache.causeway.applib.services.appfeat.ApplicationFeatureId;
import org.apache.causeway.applib.services.appfeatui.ApplicationFeatureMenu;

/**
 * @since 2.0 {@index}
 */
public class IsisAppFeatureRoleAndPermissions
extends AbstractRoleAndPermissionsFixtureScript {

    public static final String ROLE_NAME = ApplicationFeatureMenu.LOGICAL_TYPE_NAME.replace(".","-");

    public IsisAppFeatureRoleAndPermissions() {
        super(ROLE_NAME, "Access application features");
    }

    @Override
    protected void execute(ExecutionContext executionContext) {
        newPermissions(
                ApplicationPermissionRule.ALLOW,
                ApplicationPermissionMode.CHANGING,
                Can.of(
                        ApplicationFeatureId.newNamespace(CausewayModuleApplib.NAMESPACE_FEAT)
                        )
        );
    }
}
