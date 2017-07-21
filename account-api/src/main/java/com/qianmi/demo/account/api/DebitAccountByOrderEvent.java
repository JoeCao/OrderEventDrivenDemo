package com.qianmi.demo.account.api;

import org.axonframework.commandhandling.TargetAggregateIdentifier;

/**
 * Created by caozupeng on 17/7/20.
 */
public class DebitAccountByOrderEvent extends MoneySubtractedEvent {
    @TargetAggregateIdentifier
    private String bankAccountId;
    public DebitAccountByOrderEvent(String bankAccountId, long amount) {
        super(bankAccountId, amount);
        this.bankAccountId = bankAccountId;

    }
}
