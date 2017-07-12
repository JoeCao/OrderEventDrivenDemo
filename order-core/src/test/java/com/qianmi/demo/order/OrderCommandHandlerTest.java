package com.qianmi.demo.order;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.axonframework.test.aggregate.FixtureConfiguration;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by caozupeng on 17/7/12.
 */
public class OrderCommandHandlerTest {
    private FixtureConfiguration<Order> testFixture;

    @Before
    public void setUp() throws Exception {
        testFixture = new AggregateTestFixture<>(Order.class);
        testFixture.registerAnnotatedCommandHandler(
                new OrderCommandHandler(
                        testFixture.getRepository(), testFixture.getEventBus()));

    }

    @Test
    public void testOpenOrder() throws Exception {
        String id = "newTest";
        testFixture.givenNoPriorActivity()
                .when(new OpenOrderCommand(id))
                .expectEvents(new OrderOpenedEvent(id));
    }
}
