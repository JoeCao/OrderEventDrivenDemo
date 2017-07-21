package com.qianmi.demo.account.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Profile("distributed")
@EnableDiscoveryClient
@Configuration
public class DistributedConfiguration {


    @Value("${axon.amqp.exchange}")
    private String exchangeName;

    @Bean
    public Exchange exchange() {
        return ExchangeBuilder.fanoutExchange(exchangeName).durable(true).build();
    }


    @Bean
    public Queue queue() {
        return new Queue("accountqueue", true);
    }

    @Bean
    public Binding queueBinding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("").noargs();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
