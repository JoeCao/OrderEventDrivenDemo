/*
 * Copyright (c) 2016. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qianmi.demo.account.core;

import com.qianmi.demo.account.api.*;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Aggregate;
import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.commandhandling.model.Repository;
import org.axonframework.eventhandling.EventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.axonframework.eventhandling.GenericEventMessage.asEventMessage;

@Component
public class BankAccountCommandHandler {

    @Autowired
    private Repository<BankAccount> repository;
    @Autowired
    private EventBus eventBus;


    @CommandHandler
    public void handle(DebitSourceBankAccountCommand command) {
        try {
            Aggregate<BankAccount> bankAccountAggregate = repository.load(command.getBankAccountId());
            bankAccountAggregate.execute(bankAccount -> bankAccount
                    .debit(command.getAmount(), command.getBankTransferId()));
        } catch (AggregateNotFoundException exception) {
            eventBus.publish(asEventMessage(new SourceBankAccountNotFoundEvent(command.getBankTransferId())));
        }
    }

    @CommandHandler
    public void handle(CreditDestinationBankAccountCommand command) {
        try {
            Aggregate<BankAccount> bankAccountAggregate = repository.load(command.getBankAccountId());
            bankAccountAggregate.execute(bankAccount -> bankAccount
                    .credit(command.getAmount(), command.getBankTransferId()));

        }
        catch (AggregateNotFoundException exception) {
            eventBus.publish(asEventMessage(new DestinationBankAccountNotFoundEvent(command.getBankTransferId())));
        }
    }

    @CommandHandler
    public void payOrder(DebitAccountByOrderCommand command) {
        Aggregate<BankAccount> bankAccountAggregate = repository.load(command.getAccountId());
        bankAccountAggregate.execute(aggRoot -> aggRoot.payOrder(command.getTotalAmount(), command.getAccountId()));
    }
}
