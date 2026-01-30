package org.devsu.com.cuentamovimientos.application.usecase;

import org.devsu.com.cuentamovimientos.domain.entity.Cuenta;
import org.devsu.com.cuentamovimientos.domain.entity.Movimiento;
import org.devsu.com.cuentamovimientos.domain.port.CuentaRepository;
import org.devsu.com.cuentamovimientos.domain.port.MovimientoRepository;
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
@DisplayName("MovimientoService Tests")
class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private MovimientoService movimientoService;

    private Cuenta cuenta;
    private Movimiento movimiento;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta();
        cuenta.setCuentaId(1L);
        cuenta.setNumeroCuenta("1001-001-000001");
        cuenta.setSaldoInicial(BigDecimal.valueOf(1000.00));
        cuenta.setSaldoDisponible(BigDecimal.valueOf(1000.00));

        movimiento = new Movimiento();
        movimiento.setMovimientoId(1L);
        movimiento.setTipoMovimiento("Debito");
        movimiento.setValor(BigDecimal.valueOf(100.00));
        movimiento.setSaldoDisponible(BigDecimal.valueOf(900.00));
        movimiento.setCuenta(cuenta);
    }

    @Test
    @DisplayName("Debe registrar débito exitosamente")
    void testRegistrarDebito() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);

        Movimiento resultado = movimientoService.registrarDebito(1L, BigDecimal.valueOf(100.00));

        assertNotNull(resultado);
        assertEquals("Debito", resultado.getTipoMovimiento());
        assertEquals(BigDecimal.valueOf(100.00), resultado.getValor());
        verify(cuentaRepository, times(1)).findById(1L);
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
        verify(cuentaRepository, times(1)).update(cuenta);
    }

    @Test
    @DisplayName("Debe fallar débito con saldo insuficiente")
    void testRegistrarDebitoSaldoInsuficiente() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        assertThrows(IllegalArgumentException.class, () ->
            movimientoService.registrarDebito(1L, BigDecimal.valueOf(2000.00))
        );

        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    @DisplayName("Debe fallar débito si cuenta no existe")
    void testRegistrarDebitoSinCuenta() {
        when(cuentaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () ->
            movimientoService.registrarDebito(999L, BigDecimal.valueOf(100.00))
        );

        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    @DisplayName("Debe registrar crédito exitosamente")
    void testRegistrarCredito() {
        Movimiento creditoMovimiento = new Movimiento();
        creditoMovimiento.setMovimientoId(2L);
        creditoMovimiento.setTipoMovimiento("Credito");
        creditoMovimiento.setValor(BigDecimal.valueOf(500.00));
        creditoMovimiento.setSaldoDisponible(BigDecimal.valueOf(1500.00));
        creditoMovimiento.setCuenta(cuenta);

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(creditoMovimiento);

        Movimiento resultado = movimientoService.registrarCredito(1L, BigDecimal.valueOf(500.00));

        assertNotNull(resultado);
        assertEquals("Credito", resultado.getTipoMovimiento());
        assertEquals(BigDecimal.valueOf(500.00), resultado.getValor());
        verify(cuentaRepository, times(1)).findById(1L);
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    @DisplayName("Debe obtener movimiento por ID")
    void testObtenerMovimientoPorId() {
        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimiento));

        Optional<Movimiento> resultado = movimientoService.obtenerMovimientoPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Debito", resultado.get().getTipoMovimiento());
        verify(movimientoRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe obtener movimientos de una cuenta")
    void testObtenerMovimientosPorCuenta() {
        List<Movimiento> movimientos = List.of(movimiento);
        when(movimientoRepository.findByCuentaId(1L)).thenReturn(movimientos);

        List<Movimiento> resultado = movimientoService.obtenerMovimientosPorCuenta(1L);

        assertEquals(1, resultado.size());
        verify(movimientoRepository, times(1)).findByCuentaId(1L);
    }

    @Test
    @DisplayName("Debe obtener todos los movimientos")
    void testObtenerTodosLosMovimientos() {
        List<Movimiento> movimientos = List.of(movimiento);
        when(movimientoRepository.findAll()).thenReturn(movimientos);

        List<Movimiento> resultado = movimientoService.obtenerTodosLosMovimientos();

        assertEquals(1, resultado.size());
        verify(movimientoRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe actualizar saldo correctamente al registrar débito")
    void testActualizacionSaldoDebito() {
        BigDecimal saldoInicial = BigDecimal.valueOf(1000.00);
        BigDecimal debitoValor = BigDecimal.valueOf(250.00);
        BigDecimal saldoEsperado = BigDecimal.valueOf(750.00);

        cuenta.setSaldoDisponible(saldoInicial);
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimiento);

        movimientoService.registrarDebito(1L, debitoValor);

        assertEquals(saldoEsperado, cuenta.getSaldoDisponible());
        verify(cuentaRepository, times(1)).update(cuenta);
    }
}
