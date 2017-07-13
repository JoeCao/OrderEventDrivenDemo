package com.qianmi.demo.order.web;

import com.qianmi.demo.order.OpenOrderCommand;
import com.qianmi.demo.order.PayOrderCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by caozupeng on 17/7/12.
 */
@RestController
public class OrderController {
    private final CommandGateway commandGateway;

    public OrderController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @RequestMapping("/create")
    public String create() {
        String id = UUID.randomUUID().toString();
        OpenOrderCommand openOrderCommand = new OpenOrderCommand(id);
        commandGateway.send(openOrderCommand);
        return "success";
    }

    @RequestMapping(value = "/pay/{id}", method = RequestMethod.GET, produces = "application/json")
    public String payed(@PathVariable String id) {
        PayOrderCommand payOrderCommand = new PayOrderCommand(id);
        commandGateway.send(payOrderCommand);
        return id;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, String> get() {
        return new HashMap<>();
    }

}
