package com.qianmi.demo.account.api;

import lombok.Value;
import org.axonframework.commandhandling.TargetAggregateIdentifier;

/**
 * Created by caozupeng on 17/7/20.
 */
@Value
public class DebitAccountByOrderCommand {


    @TargetAggregateIdentifier
    private String orderId;

    private String accountId;
    private long totalAmount;
}
