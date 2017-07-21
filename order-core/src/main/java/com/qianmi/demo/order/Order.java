package com.qianmi.demo.order;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.spring.stereotype.Aggregate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;

/**
 * Created by caozupeng on 17/7/12.
 */
@Aggregate
public class Order {
    @Id
    private String orderId;
    @Column(name = "status", nullable = false)
    private Status status;

    @SuppressWarnings("unused")
    private Order() {

    }

    @CommandHandler
    public Order(OpenOrderCommand command) {
        apply(new OrderOpenedEvent(command.getOrderId(), "6ecb080f-50ac-469f-887c-f3524eaf1ae6", 1));
    }

    @CommandHandler
    public void payed(PayOrderCommand command) {
        //TODO check the order status, can pay?
        apply(new OrderPayedEvent(command.getOrderId()));
    }

    @EventHandler
    public void on(OrderPayedEvent event) {
        this.status = Status.PAYED;
    }

    @EventHandler
    public void on(OrderOpenedEvent event) {
        this.orderId = event.getId();
        this.status = Status.OPENED;
    }

    private enum Status {
        OPENED,
        CANCELED,
        PAYED,
        COMPLETED
    }
}
