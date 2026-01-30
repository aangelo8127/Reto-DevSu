package org.devsu.com.cuentamovimientos.application.usecase;

import org.devsu.com.cuentamovimientos.domain.entity.Cuenta;
import org.devsu.com.cuentamovimientos.domain.port.CuentaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CuentaService Tests")
class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private CuentaService cuentaService;

    private Cuenta cuenta;

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
    }

    @Test
    @DisplayName("Debe crear una cuenta exitosamente")
    void testCrearCuenta() {
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        Cuenta resultado = cuentaService.crearCuenta(cuenta);

        assertNotNull(resultado);
        assertEquals("1001-001-000001", resultado.getNumeroCuenta());
        assertEquals("Ahorros", resultado.getTipoCuenta());
        verify(cuentaRepository, times(1)).save(cuenta);
    }

    @Test
    @DisplayName("Debe obtener cuenta por ID")
    void testObtenerCuentaPorId() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        Optional<Cuenta> resultado = cuentaService.obtenerCuentaPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("1001-001-000001", resultado.get().getNumeroCuenta());
        verify(cuentaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe obtener cuenta por número de cuenta")
    void testObtenerCuentaPorNumero() {
        when(cuentaRepository.findByNumeroCuenta("1001-001-000001")).thenReturn(Optional.of(cuenta));

        Optional<Cuenta> resultado = cuentaService.obtenerCuentaPorNumero("1001-001-000001");

        assertTrue(resultado.isPresent());
        assertEquals("1001-001-000001", resultado.get().getNumeroCuenta());
        verify(cuentaRepository, times(1)).findByNumeroCuenta("1001-001-000001");
    }

    @Test
    @DisplayName("Debe obtener cuentas de un cliente")
    void testObtenerCuentasPorCliente() {
        Cuenta cuenta2 = new Cuenta();
        cuenta2.setCuentaId(2L);
        cuenta2.setNumeroCuenta("1001-001-000002");
        cuenta2.setClienteId("CLIENTE001");

        List<Cuenta> cuentas = List.of(cuenta, cuenta2);
        when(cuentaRepository.findByClienteId("CLIENTE001")).thenReturn(cuentas);

        List<Cuenta> resultado = cuentaService.obtenerCuentasPorCliente("CLIENTE001");

        assertEquals(2, resultado.size());
        verify(cuentaRepository, times(1)).findByClienteId("CLIENTE001");
    }

    @Test
    @DisplayName("Debe obtener todas las cuentas")
    void testObtenerTodasLasCuentas() {
        Cuenta cuenta2 = new Cuenta();
        cuenta2.setCuentaId(2L);
        cuenta2.setNumeroCuenta("1001-001-000002");

        List<Cuenta> cuentas = List.of(cuenta, cuenta2);
        when(cuentaRepository.findAll()).thenReturn(cuentas);

        List<Cuenta> resultado = cuentaService.obtenerTodasLasCuentas();

        assertEquals(2, resultado.size());
        verify(cuentaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe actualizar cuenta exitosamente")
    void testActualizarCuenta() {
        cuenta.setTipoCuenta("Corriente");

        cuentaService.actualizarCuenta(cuenta);

        verify(cuentaRepository, times(1)).update(cuenta);
    }

    @Test
    @DisplayName("Debe eliminar cuenta exitosamente")
    void testEliminarCuenta() {
        cuentaService.eliminarCuenta(1L);

        verify(cuentaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Debe retornar vacío cuando cuenta no existe")
    void testObtenerCuentaPorIdNoExiste() {
        when(cuentaRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Cuenta> resultado = cuentaService.obtenerCuentaPorId(999L);

        assertFalse(resultado.isPresent());
        verify(cuentaRepository, times(1)).findById(999L);
    }
}
