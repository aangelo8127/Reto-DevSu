package org.devsu.com.clientepersona.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CLIENTE_EXCHANGE = "cliente.exchange";

    public static final String CLIENTE_CREATED_QUEUE = "cliente.created.queue";
    public static final String CLIENTE_UPDATED_QUEUE = "cliente.updated.queue";
    public static final String CLIENTE_DELETED_QUEUE = "cliente.deleted.queue";

    public static final String CLIENTE_CREATED_KEY = "cliente.created";
    public static final String CLIENTE_UPDATED_KEY = "cliente.updated";
    public static final String CLIENTE_DELETED_KEY = "cliente.deleted";

    @Bean
    public DirectExchange clienteExchange() {
        return new DirectExchange(CLIENTE_EXCHANGE, true, false);
    }

    @Bean
    public Queue clienteCreatedQueue() {
        return new Queue(CLIENTE_CREATED_QUEUE, true);
    }

    @Bean
    public Queue clienteUpdatedQueue() {
        return new Queue(CLIENTE_UPDATED_QUEUE, true);
    }

    @Bean
    public Queue clienteDeletedQueue() {
        return new Queue(CLIENTE_DELETED_QUEUE, true);
    }

    @Bean
    public Binding clienteCreatedBinding(Queue clienteCreatedQueue, DirectExchange clienteExchange) {
        return BindingBuilder.bind(clienteCreatedQueue)
                .to(clienteExchange)
                .with(CLIENTE_CREATED_KEY);
    }

    @Bean
    public Binding clienteUpdatedBinding(Queue clienteUpdatedQueue, DirectExchange clienteExchange) {
        return BindingBuilder.bind(clienteUpdatedQueue)
                .to(clienteExchange)
                .with(CLIENTE_UPDATED_KEY);
    }

    @Bean
    public Binding clienteDeletedBinding(Queue clienteDeletedQueue, DirectExchange clienteExchange) {
        return BindingBuilder.bind(clienteDeletedQueue)
                .to(clienteExchange)
                .with(CLIENTE_DELETED_KEY);
    }

}
