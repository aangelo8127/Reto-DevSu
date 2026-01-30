package org.devsu.com.clientepersona.infrastructure.controller;

import org.devsu.com.clientepersona.application.usecase.ClienteService;
import org.devsu.com.clientepersona.domain.entity.Cliente;
import org.devsu.com.clientepersona.infrastructure.dto.ClienteRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClienteController.class)
@DisplayName("ClienteController Tests")
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    private Cliente cliente;
    private ClienteRequest clienteRequest;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setPersonaId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setGenero("Masculino");
        cliente.setEdad(35);
        cliente.setIdentificacion("1234567890");
        cliente.setDireccion("Calle Principal 123");
        cliente.setTelefono("5551234567");
        cliente.setClienteId("CLIENTE001");
        cliente.setContrasena("password123");
        cliente.setEstado(true);

        clienteRequest = new ClienteRequest();
        clienteRequest.setNombre("Juan Pérez");
        clienteRequest.setGenero("Masculino");
        clienteRequest.setEdad(35);
        clienteRequest.setIdentificacion("1234567890");
        clienteRequest.setDireccion("Calle Principal 123");
        clienteRequest.setTelefono("5551234567");
        clienteRequest.setClienteId("CLIENTE001");
        clienteRequest.setContrasena("password123");
        clienteRequest.setEstado(true);
    }

    @Test
    @DisplayName("GET /api/clientes - Debe retornar lista de clientes")
    void testObtenerTodos() throws Exception {
        when(clienteService.obtenerTodosLosClientes()).thenReturn(List.of(cliente));

        mockMvc.perform(get("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombre").value("Juan Pérez"));

        verify(clienteService, times(1)).obtenerTodosLosClientes();
    }

    @Test
    @DisplayName("GET /api/clientes/{id} - Debe retornar cliente por ID")
    void testObtenerPorId() throws Exception {
        when(clienteService.obtenerClientePorId(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value("CLIENTE001"));

        verify(clienteService, times(1)).obtenerClientePorId(1L);
    }

    @Test
    @DisplayName("GET /api/clientes/{id} - Debe retornar 404 cuando no existe")
    void testObtenerPorIdNoExiste() throws Exception {
        when(clienteService.obtenerClientePorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/clientes/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).obtenerClientePorId(999L);
    }

    @Test
    @DisplayName("POST /api/clientes - Debe crear nuevo cliente")
    void testCrear() throws Exception {
        when(clienteService.crearCliente(any(Cliente.class))).thenReturn(cliente);

        mockMvc.perform(post("/api/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId").value("CLIENTE001"));

        verify(clienteService, times(1)).crearCliente(any(Cliente.class));
    }

    @Test
    @DisplayName("PUT /api/clientes/{id} - Debe actualizar cliente")
    void testActualizar() throws Exception {
        when(clienteService.obtenerClientePorId(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(put("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(clienteRequest)))
                .andExpect(status().isNoContent());

        verify(clienteService, times(1)).obtenerClientePorId(1L);
        verify(clienteService, times(1)).actualizarCliente(any(Cliente.class));
    }

    @Test
    @DisplayName("DELETE /api/clientes/{id} - Debe eliminar cliente")
    void testEliminar() throws Exception {
        mockMvc.perform(delete("/api/clientes/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(clienteService, times(1)).eliminarCliente(1L);
    }
}
