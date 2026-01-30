package org.devsu.com.cuentamovimientos.infrastructure.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CLIENTE_EXCHANGE = "cliente.exchange";

    public static final String CUENTA_CLIENTE_CREATED_QUEUE = "cuenta.cliente.created.queue";
    public static final String CUENTA_CLIENTE_UPDATED_QUEUE = "cuenta.cliente.updated.queue";
    public static final String CUENTA_CLIENTE_DELETED_QUEUE = "cuenta.cliente.deleted.queue";

    public static final String CLIENTE_CREATED_KEY = "cliente.created";
    public static final String CLIENTE_UPDATED_KEY = "cliente.updated";
    public static final String CLIENTE_DELETED_KEY = "cliente.deleted";

    @Bean
    public DirectExchange clienteExchange() {
        return new DirectExchange(CLIENTE_EXCHANGE, true, false);
    }

    @Bean
    public Queue cuentaClienteCreatedQueue() {
        return new Queue(CUENTA_CLIENTE_CREATED_QUEUE, true);
    }

    @Bean
    public Queue cuentaClienteUpdatedQueue() {
        return new Queue(CUENTA_CLIENTE_UPDATED_QUEUE, true);
    }

    @Bean
    public Queue cuentaClienteDeletedQueue() {
        return new Queue(CUENTA_CLIENTE_DELETED_QUEUE, true);
    }

    @Bean
    public Binding cuentaClienteCreatedBinding(Queue cuentaClienteCreatedQueue, DirectExchange clienteExchange) {
        return BindingBuilder.bind(cuentaClienteCreatedQueue)
                .to(clienteExchange)
                .with(CLIENTE_CREATED_KEY);
    }

    @Bean
    public Binding cuentaClienteUpdatedBinding(Queue cuentaClienteUpdatedQueue, DirectExchange clienteExchange) {
        return BindingBuilder.bind(cuentaClienteUpdatedQueue)
                .to(clienteExchange)
                .with(CLIENTE_UPDATED_KEY);
    }

    @Bean
    public Binding cuentaClienteDeletedBinding(Queue cuentaClienteDeletedQueue, DirectExchange clienteExchange) {
        return BindingBuilder.bind(cuentaClienteDeletedQueue)
                .to(clienteExchange)
                .with(CLIENTE_DELETED_KEY);
    }

}
