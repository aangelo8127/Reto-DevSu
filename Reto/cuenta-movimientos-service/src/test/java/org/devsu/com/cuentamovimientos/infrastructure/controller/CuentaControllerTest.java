package org.devsu.com.cuentamovimientos.infrastructure.controller;

import org.devsu.com.cuentamovimientos.application.usecase.CuentaService;
import org.devsu.com.cuentamovimientos.domain.entity.Cuenta;
import org.devsu.com.cuentamovimientos.infrastructure.dto.CuentaRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
@DisplayName("CuentaController Tests")
class CuentaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CuentaService cuentaService;

    @Autowired
    private ObjectMapper objectMapper;

    private Cuenta cuenta;
    private CuentaRequest cuentaRequest;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta();
        cuenta.setCuentaId(1L);
        cuenta.setNumeroCuenta("1001-001-000001");
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setSaldoInicial(BigDecimal.valueOf(1000.00));
        cuenta.setSaldoDisponible(BigDecimal.valueOf(1000.00));
        cuenta.setEstado(true);
        cuenta.setClienteId("CLIENTE001");

        cuentaRequest = new CuentaRequest();
        cuentaRequest.setNumeroCuenta("1001-001-000001");
        cuentaRequest.setTipoCuenta("Ahorros");
        cuentaRequest.setSaldoInicial(BigDecimal.valueOf(1000.00));
        cuentaRequest.setClienteId("CLIENTE001");
        cuentaRequest.setEstado(true);
    }

    @Test
    @DisplayName("GET /api/cuentas - Debe retornar lista de cuentas")
    void testObtenerTodas() throws Exception {
        when(cuentaService.obtenerTodasLasCuentas()).thenReturn(List.of(cuenta));

        mockMvc.perform(get("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].numeroCuenta").value("1001-001-000001"));

        verify(cuentaService, times(1)).obtenerTodasLasCuentas();
    }

    @Test
    @DisplayName("GET /api/cuentas/{id} - Debe retornar cuenta por ID")
    void testObtenerPorId() throws Exception {
        when(cuentaService.obtenerCuentaPorId(1L)).thenReturn(Optional.of(cuenta));

        mockMvc.perform(get("/api/cuentas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tipoCuenta").value("Ahorros"));

        verify(cuentaService, times(1)).obtenerCuentaPorId(1L);
    }

    @Test
    @DisplayName("GET /api/cuentas/{id} - Debe retornar 404 cuando no existe")
    void testObtenerPorIdNoExiste() throws Exception {
        when(cuentaService.obtenerCuentaPorId(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/cuentas/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(cuentaService, times(1)).obtenerCuentaPorId(999L);
    }

    @Test
    @DisplayName("GET /api/cuentas/numero/{numeroCuenta} - Debe retornar cuenta por número")
    void testObtenerPorNumero() throws Exception {
        when(cuentaService.obtenerCuentaPorNumero("1001-001-000001")).thenReturn(Optional.of(cuenta));

        mockMvc.perform(get("/api/cuentas/numero/1001-001-000001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cuentaId").value(1));

        verify(cuentaService, times(1)).obtenerCuentaPorNumero("1001-001-000001");
    }

    @Test
    @DisplayName("GET /api/cuentas/cliente/{clienteId} - Debe retornar cuentas del cliente")
    void testObtenerPorCliente() throws Exception {
        when(cuentaService.obtenerCuentasPorCliente("CLIENTE001")).thenReturn(List.of(cuenta));

        mockMvc.perform(get("/api/cuentas/cliente/CLIENTE001")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].clienteId").value("CLIENTE001"));

        verify(cuentaService, times(1)).obtenerCuentasPorCliente("CLIENTE001");
    }

    @Test
    @DisplayName("POST /api/cuentas - Debe crear nueva cuenta")
    void testCrear() throws Exception {
        when(cuentaService.crearCuenta(any(Cuenta.class))).thenReturn(cuenta);

        mockMvc.perform(post("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuentaRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.numeroCuenta").value("1001-001-000001"));

        verify(cuentaService, times(1)).crearCuenta(any(Cuenta.class));
    }

    @Test
    @DisplayName("PUT /api/cuentas/{id} - Debe actualizar cuenta")
    void testActualizar() throws Exception {
        when(cuentaService.obtenerCuentaPorId(1L)).thenReturn(Optional.of(cuenta));

        mockMvc.perform(put("/api/cuentas/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cuentaRequest)))
                .andExpect(status().isNoContent());

        verify(cuentaService, times(1)).obtenerCuentaPorId(1L);
        verify(cuentaService, times(1)).actualizarCuenta(any(Cuenta.class));
    }

    @Test
    @DisplayName("DELETE /api/cuentas/{id} - Debe eliminar cuenta")
    void testEliminar() throws Exception {
        mockMvc.perform(delete("/api/cuentas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(cuentaService, times(1)).eliminarCuenta(1L);
    }
}
