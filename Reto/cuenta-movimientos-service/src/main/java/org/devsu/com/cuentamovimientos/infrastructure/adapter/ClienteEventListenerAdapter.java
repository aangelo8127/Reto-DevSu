package org.devsu.com.cuentamovimientos.infrastructure.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * Adaptador: ClienteEventListenerAdapter
 * Escucha eventos de Cliente publicados por el Cliente-Persona Service.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ClienteEventListenerAdapter {

    private final ObjectMapper objectMapper;

    /**
     * Escucha eventos cuando se crea un cliente.
     */
    @RabbitListener(queues = "cuenta.cliente.created.queue")
    public void onClienteCreated(String message) {
        try {
            log.info("Evento ClienteCreated recibido: {}", message);
            // Aquí se puede realizar lógica adicional, como crear una cuenta por defecto
        } catch (Exception e) {
            log.error("Error procesando evento ClienteCreated", e);
        }
    }

    @RabbitListener(queues = "cuenta.cliente.updated.queue")
    public void onClienteUpdated(String message) {
        try {
            log.info("Evento ClienteUpdated recibido: {}", message);
        } catch (Exception e) {
            log.error("Error procesando evento ClienteUpdated", e);
        }
    }

    @RabbitListener(queues = "cuenta.cliente.deleted.queue")
    public void onClienteDeleted(String clienteId) {
        try {
            log.info("Evento ClienteDeleted recibido para cliente: {}", clienteId);
        } catch (Exception e) {
            log.error("Error procesando evento ClienteDeleted", e);
        }
    }

}
