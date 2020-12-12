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
package org.apache.isis.tooling.j2adoc.format;

import org.asciidoctor.ast.Document;
import org.asciidoctor.ast.List;
import org.asciidoctor.ast.StructuralNode;

import org.apache.isis.tooling.j2adoc.J2AdocContext;
import org.apache.isis.tooling.model4adoc.AsciiDocFactory;

import lombok.val;

public class UnitFormatterCompact 
extends UnitFormatterAbstract {

    public UnitFormatterCompact(J2AdocContext j2aContext) {
        super(j2aContext);
    }

    @Override
    protected StructuralNode getMemberDescriptionContainer(StructuralNode parent) {
        val ul = AsciiDocFactory.list(parent);
        return ul;
    }

    @Override
    protected void appendMemberDescription(StructuralNode ul, String member, Document javadoc) {
        val li = AsciiDocFactory.listItem((List) ul, member);
        val openBlock = AsciiDocFactory.openBlock(li);
        val javaDocBlock = AsciiDocFactory.block(openBlock);
        javaDocBlock.getBlocks().addAll(javadoc.getBlocks());
    }

}
