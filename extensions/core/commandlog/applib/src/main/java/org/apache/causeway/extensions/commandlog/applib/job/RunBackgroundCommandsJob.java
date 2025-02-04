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
package org.apache.causeway.extensions.commandlog.applib.job;

import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;

import org.apache.causeway.applib.services.command.CommandExecutorService;
import org.apache.causeway.applib.services.iactnlayer.InteractionContext;
import org.apache.causeway.applib.services.iactnlayer.InteractionService;
import org.apache.causeway.applib.services.user.UserMemento;
import org.apache.causeway.applib.services.xactn.TransactionService;
import org.apache.causeway.applib.util.schema.CommandDtoUtils;
import org.apache.causeway.commons.functional.ThrowingRunnable;
import org.apache.causeway.extensions.commandlog.applib.dom.CommandLogEntry;
import org.apache.causeway.extensions.commandlog.applib.dom.CommandLogEntryRepository;

import lombok.val;
import lombok.extern.log4j.Log4j2;

/**
 * An implementation of a Quartz {@link Job} that queries for {@link CommandLogEntry}s that have been persisted by
 * the {@link org.apache.causeway.extensions.commandlog.applib.dom.BackgroundService} but not yet started; and then
 * executes them.
 *
 * @since 2.0 {@index}
 */
@Component
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
@Log4j2
public class RunBackgroundCommandsJob implements Job {

    @Inject InteractionService interactionService;
    @Inject TransactionService transactionService;
    @Inject CommandLogEntryRepository<? extends CommandLogEntry> commandLogEntryRepository;
    @Inject CommandExecutorService commandExecutorService;

    @Override
    public void execute(final JobExecutionContext quartzContext) {
        val user = UserMemento.ofNameAndRoleNames("scheduler_user", "admin_role");
        val interactionContext = InteractionContext.builder().user(user).build();
        interactionService.run(interactionContext, new ExecuteNotYetStartedCommands());
    }

    private class ExecuteNotYetStartedCommands implements ThrowingRunnable {

        @Override
        public void run() {

            // we obtain the list of Commands first; we use their CommandDto as it is serializable across transactions
            val commandDtosIfAny =
                    transactionService.callTransactional(
                            Propagation.REQUIRES_NEW,
                            () -> commandLogEntryRepository.findBackgroundAndNotYetStarted()
                                    .stream()
                                    .map(CommandLogEntry::getCommandDto)
                                    .collect(Collectors.toList())
                    )
                    .ifFailureFail()    // we give up if unable to find these
                    .getValue();        // the success case wrapped in an optional

            // for each command, we execute within its own transaction.  Failure of one should not impact the next.
            commandDtosIfAny.ifPresent(
                commandDtos -> {
                for (val commandDto : commandDtos) {
                    transactionService.runTransactional(Propagation.REQUIRES_NEW, () -> {
                        // it's necessary to look up the CommandLogEntry again because we are within a new transaction.
                        val commandLogEntryIfAny = commandLogEntryRepository.findByInteractionId(UUID.fromString(commandDto.getInteractionId()));

                        // finally, we execute
                        commandLogEntryIfAny.ifPresent(commandLogEntry ->
                                commandExecutorService.executeCommand(
                                        CommandExecutorService.InteractionContextPolicy.NO_SWITCH, commandDto));
                    }).ifFailure(throwable -> log.error("Failed to execute command: " +
                            CommandDtoUtils.dtoMapper().toString(commandDto), throwable));
                }
            });
        }
    }

}
