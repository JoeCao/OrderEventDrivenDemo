/*
 * Copyright (c) 2016. Axon Framework
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.qianmi.demo.order.config;

import com.qianmi.demo.order.Order;
import com.qianmi.demo.order.OrderCommandHandler;
import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.SimpleCommandBus;
import org.axonframework.commandhandling.model.GenericJpaRepository;
import org.axonframework.common.jpa.ContainerManagedEntityManagerProvider;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.monitoring.NoOpMessageMonitor;
import org.axonframework.spring.config.AxonConfiguration;
import org.axonframework.spring.messaging.unitofwork.SpringTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class AxonConfig {

    @Autowired
    private AxonConfiguration axonConfiguration;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }

    @Bean
    public OrderCommandHandler bankAccountCommandHandler() {
        return new OrderCommandHandler(orderAggRepository(), eventBus());
    }

    @Bean
    public TransactionManager axonTransactionManager() {
        return new SpringTransactionManager(transactionManager);
    }

    @Bean
    public CommandBus commandBus() {
        SimpleCommandBus commandBus = new SimpleCommandBus(axonTransactionManager(), NoOpMessageMonitor.INSTANCE);
        return commandBus;
    }

    @Bean
    public EntityManagerProvider entityManagerProvider() {
        return new ContainerManagedEntityManagerProvider();
    }

    @Bean
    public GenericJpaRepository<Order> orderAggRepository() {
        return new GenericJpaRepository<Order>(entityManagerProvider(), Order.class, eventBus());
    }
//    @Bean
//    public SagaConfiguration bankTransferManagementSagaConfiguration() {
//        return SagaConfiguration.trackingSagaManager(BankTransferManagementSaga.class);
//    }
//
//    @Autowired
//    public void configure(@Qualifier("localSegment") SimpleCommandBus simpleCommandBus) {
//        simpleCommandBus.registerDispatchInterceptor(new BeanValidationInterceptor<>());
//    }
}
