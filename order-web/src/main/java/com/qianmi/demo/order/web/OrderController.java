package com.qianmi.demo.order.web;

import com.qianmi.demo.order.OpenOrderCommand;
import com.qianmi.demo.order.PayOrderCommand;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by caozupeng on 17/7/12.
 */
@Controller
public class OrderController {
    private final CommandGateway commandGateway;

    public OrderController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @RequestMapping(value = {"/create"})
    public ModelAndView create() {
        String id = UUID.randomUUID().toString();
        OpenOrderCommand openOrderCommand = new OpenOrderCommand(id);
        commandGateway.send(openOrderCommand);
        return new ModelAndView("/");
    }

    @RequestMapping(path = "/pay/{id}", method = RequestMethod.GET)
    public ModelAndView payed(String id) {
        PayOrderCommand payOrderCommand = new PayOrderCommand(id);
        commandGateway.send(payOrderCommand);
        return new ModelAndView("/");
    }

    @RequestMapping(method = RequestMethod.GET)
    public Map<String, String> get() {
        return new HashMap<>();
    }

}
