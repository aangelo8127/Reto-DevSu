package org.devsu.com.clientepersona.infrastructure.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.devsu.com.clientepersona.domain.entity.Cliente;
import org.devsu.com.clientepersona.domain.port.ClienteEventPublisher;
import org.devsu.com.clientepersona.infrastructure.config.RabbitMQConfig;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ClienteEventPublisherAdapter implements ClienteEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void publishClienteCreated(Cliente cliente) {
        try {
            String message = objectMapper.writeValueAsString(cliente);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.CLIENTE_EXCHANGE,
                    RabbitMQConfig.CLIENTE_CREATED_KEY,
                    message
            );
            log.info("Evento ClienteCreated publicado para cliente: {}", cliente.getClienteId());
        } catch (Exception e) {
            log.error("Error al publicar evento ClienteCreated", e);
        }
    }

    @Override
    public void publishClienteUpdated(Cliente cliente) {
        try {
            String message = objectMapper.writeValueAsString(cliente);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.CLIENTE_EXCHANGE,
                    RabbitMQConfig.CLIENTE_UPDATED_KEY,
                    message
            );
            log.info("Evento ClienteUpdated publicado para cliente: {}", cliente.getClienteId());
        } catch (Exception e) {
            log.error("Error al publicar evento ClienteUpdated", e);
        }
    }

    @Override
    public void publishClienteDeleted(String clienteId) {
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.CLIENTE_EXCHANGE,
                    RabbitMQConfig.CLIENTE_DELETED_KEY,
                    clienteId
            );
            log.info("Evento ClienteDeleted publicado para cliente: {}", clienteId);
        } catch (Exception e) {
            log.error("Error al publicar evento ClienteDeleted", e);
        }
    }

}
