package org.devsu.com.cuentamovimientos.infrastructure.adapter;

import org.devsu.com.cuentamovimientos.domain.entity.Cuenta;
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
@DisplayName("CuentaRepositoryAdapter Tests")
class CuentaRepositoryAdapterTest {

    @Mock
    private CuentaJpaRepository cuentaJpaRepository;

    @InjectMocks
    private CuentaRepositoryAdapter cuentaRepositoryAdapter;

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
    @DisplayName("Debe guardar cuenta mediante JPA")
    void testSave() {
        when(cuentaJpaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        Cuenta resultado = cuentaRepositoryAdapter.save(cuenta);

        assertNotNull(resultado);
        assertEquals("1001-001-000001", resultado.getNumeroCuenta());
        verify(cuentaJpaRepository, times(1)).save(cuenta);
    }

    @Test
    @DisplayName("Debe obtener cuenta por ID mediante JPA")
    void testFindById() {
        when(cuentaJpaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        Optional<Cuenta> resultado = cuentaRepositoryAdapter.findById(1L);

        assertTrue(resultado.isPresent());
        assertEquals("1001-001-000001", resultado.get().getNumeroCuenta());
        verify(cuentaJpaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe obtener cuenta por número mediante JPA")
    void testFindByNumeroCuenta() {
        when(cuentaJpaRepository.findByNumeroCuenta("1001-001-000001")).thenReturn(Optional.of(cuenta));

        Optional<Cuenta> resultado = cuentaRepositoryAdapter.findByNumeroCuenta("1001-001-000001");

        assertTrue(resultado.isPresent());
        verify(cuentaJpaRepository, times(1)).findByNumeroCuenta("1001-001-000001");
    }

    @Test
    @DisplayName("Debe obtener cuentas por cliente mediante JPA")
    void testFindByClienteId() {
        List<Cuenta> cuentas = List.of(cuenta);
        when(cuentaJpaRepository.findByClienteId("CLIENTE001")).thenReturn(cuentas);

        List<Cuenta> resultado = cuentaRepositoryAdapter.findByClienteId("CLIENTE001");

        assertEquals(1, resultado.size());
        verify(cuentaJpaRepository, times(1)).findByClienteId("CLIENTE001");
    }

    @Test
    @DisplayName("Debe obtener todas las cuentas mediante JPA")
    void testFindAll() {
        List<Cuenta> cuentas = List.of(cuenta);
        when(cuentaJpaRepository.findAll()).thenReturn(cuentas);

        List<Cuenta> resultado = cuentaRepositoryAdapter.findAll();

        assertEquals(1, resultado.size());
        verify(cuentaJpaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe actualizar cuenta mediante JPA")
    void testUpdate() {
        cuentaRepositoryAdapter.update(cuenta);

        verify(cuentaJpaRepository, times(1)).save(cuenta);
    }

    @Test
    @DisplayName("Debe eliminar cuenta por ID mediante JPA")
    void testDeleteById() {
        cuentaRepositoryAdapter.deleteById(1L);

        verify(cuentaJpaRepository, times(1)).deleteById(1L);
    }
}
