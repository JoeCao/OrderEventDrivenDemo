package com.qianmi.demo.order;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by caozupeng on 17/7/19.
 */
@Saga
public class CreateOrderSaga {

    private transient CommandBus commandBus;

    @Autowired
    public void setCommandBus(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

}
