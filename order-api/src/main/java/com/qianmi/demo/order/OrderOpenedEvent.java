package com.qianmi.demo.order;

import lombok.Value;

/**
 * Created by caozupeng on 17/7/12.
 */
@Value
public class OrderOpenedEvent {
    private String id;
    private String accountId;
    private long totalAmount;
}
