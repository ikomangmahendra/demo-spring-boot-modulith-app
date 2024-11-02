package com.angjaya.core.demospringmodulithapp.orders;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Set;

@Controller
@ResponseBody
@RequestMapping("/orders")
class OrdersController {

    private final Orders orders;

    OrdersController(Orders orders) {
        this.orders = orders;
    }

    @PostMapping
    void place(@RequestBody Order order) {
        this.orders.placeOrder(order);
    }
}

@Service
@Transactional
class Orders {

    private final OrderRepository repository;
    private final ApplicationEventPublisher eventPublisher;

    Orders(OrderRepository repository, ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.eventPublisher = eventPublisher;
    }

    void placeOrder(Order order) {
        var saved = repository.save(order);
        System.out.println("saved " + saved);

        this.eventPublisher.publishEvent(new OrderPlaceEvent(saved.id()));
    }
}

interface OrderRepository extends ListCrudRepository<Order, Long> {
}

@Table("orders")
record Order(@Id Long id, Set<LineItem> lineItems) {

}

@Table("orders_line_items")
record LineItem(@Id Long id, int product, int quantity) {

}


@Configuration
class AmqpIntegrationConfiguration {

    static final String ORDERS_Q = "orders";

    @Bean
    Binding binding(Queue queue, Exchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ORDERS_Q).noargs();
    }

    @Bean
    Exchange exchange() {
        return ExchangeBuilder.directExchange(ORDERS_Q).build();
    }

    @Bean
    Queue queue() {
        return QueueBuilder.durable(ORDERS_Q).build();
    }
}