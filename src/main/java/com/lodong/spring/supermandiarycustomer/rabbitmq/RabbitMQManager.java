package com.lodong.spring.supermandiarycustomer.rabbitmq;

import com.lodong.spring.supermandiarycustomer.enumvalue.Exchange;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RabbitMQManager {

    private final AmqpAdmin amqpAdmin;

    public void declareQueue(String username, boolean durable) {
        Queue queue = new Queue(username, durable);
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(binding(queue, topicExchange(), username));
    }

    private TopicExchange topicExchange() {
        return new TopicExchange(Exchange.EXCHANGE.getExchange());
    }

    private Binding binding(Queue queue, TopicExchange topicExchange, String routingKey) {
        return BindingBuilder
                .bind(queue)
                .to(topicExchange)
                .with(routingKey);
    }
}