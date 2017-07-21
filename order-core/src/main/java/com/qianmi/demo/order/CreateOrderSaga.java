package com.qianmi.demo.order;

import com.qianmi.demo.account.api.DebitAccountByOrderCommand;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.LoggingCallback;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.eventhandling.saga.SagaEventHandler;
import org.axonframework.eventhandling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

/**
 * Created by caozupeng on 17/7/19.
 */
@Saga
public class CreateOrderSaga {

    private String orderId;
    private long amount;
    private String accountId;

    @Autowired
    private transient CommandGateway commandGateway;

    @StartSaga
    @SagaEventHandler(associationProperty = "id")
    public void on(OrderOpenedEvent event) {
        this.accountId = event.getAccountId();
        this.amount = event.getTotalAmount();
        DebitAccountByOrderCommand command =
                new DebitAccountByOrderCommand(
                        event.getId(),
                        event.getAccountId(),
                        event.getTotalAmount());
        commandGateway.send(asCommandMessage(command), LoggingCallback.INSTANCE);

    }

}
