package com.qianmi.demo.order.config;

import com.qianmi.demo.order.CreateOrderSaga;
import com.rabbitmq.client.Channel;
import org.axonframework.amqp.eventhandling.spring.SpringAMQPMessageSource;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.distributed.AnnotationRoutingStrategy;
import org.axonframework.commandhandling.distributed.CommandBusConnector;
import org.axonframework.commandhandling.distributed.CommandRouter;
import org.axonframework.commandhandling.distributed.DistributedCommandBus;
import org.axonframework.config.SagaConfiguration;
import org.axonframework.serialization.Serializer;
import org.axonframework.springcloud.commandhandling.SpringCloudCommandRouter;
import org.axonframework.springcloud.commandhandling.SpringHttpCommandBusConnector;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestOperations;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;

import static org.slf4j.LoggerFactory.getLogger;

@Profile("distributed")
@EnableDiscoveryClient
@Configuration
public class DistributedConfiguration {
    // Example function providing a Spring Cloud Connector
    private static final Logger LOGGER = getLogger(DistributedConfiguration.class);
    @Value("${axon.amqp.exchange}")
    private String exchangeName;
    @Bean
    public Queue queue(){
        return new Queue("orderqueue", true);
    }

    @Bean
    public Exchange exchange(){
        return ExchangeBuilder.fanoutExchange(exchangeName).durable(true).build();
    }

    @Bean
    public Binding queueBinding() {
        return BindingBuilder.bind(queue()).to(exchange()).with("").noargs();
    }


    @Bean
    public SpringAMQPMessageSource queueMessageSource(Serializer serializer){
        return new SpringAMQPMessageSource(serializer){
            @RabbitListener(queues = "orderqueue")
            @Override
            @Transactional
            public void onMessage(Message message, Channel channel) throws Exception {
                LOGGER.debug("Message received: "+message.toString());
                super.onMessage(message, channel);
            }
        };
    }

    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    public SagaConfiguration<CreateOrderSaga> orderSagaConfiguration(Serializer serializer){
        SagaConfiguration<CreateOrderSaga> sagaConfiguration = SagaConfiguration.subscribingSagaManager(CreateOrderSaga.class, c-> queueMessageSource(serializer));
        //sagaConfiguration.registerHandlerInterceptor(c->transactionManagingInterceptor());
        return sagaConfiguration;
    }


}
