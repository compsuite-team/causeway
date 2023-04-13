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
package org.apache.causeway.testdomain.model.good;

import jakarta.inject.Named;

import org.apache.causeway.applib.annotation.Property;

@Named("causeway.testdomain.ProperInterface2")
public interface ProperInterface2
extends ProperInterface {

    @Property
    default String getE() {
        return "e";
    }
    default void setE(final String prop) {
        // no-op, just testing meta-data
    }

    @Property
    String getF();
    void setF(String prop);

}
