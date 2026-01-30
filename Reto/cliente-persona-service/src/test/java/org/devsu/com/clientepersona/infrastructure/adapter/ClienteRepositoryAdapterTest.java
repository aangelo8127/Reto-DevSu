package org.devsu.com.clientepersona.infrastructure.adapter;

import org.devsu.com.clientepersona.domain.entity.Cliente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClienteRepositoryAdapter Tests")
class ClienteRepositoryAdapterTest {

    @Mock
    private ClienteJpaRepository clienteJpaRepository;

    @InjectMocks
    private ClienteRepositoryAdapter clienteRepositoryAdapter;

    private Cliente cliente;

    @BeforeEach
    void setUp() {
        cliente = new Cliente();
        cliente.setPersonaId(1L);
        cliente.setNombre("Juan Pérez");
        cliente.setClienteId("CLIENTE001");
    }

    @Test
    @DisplayName("Debe guardar cliente mediante JPA")
    void testSave() {
        when(clienteJpaRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente resultado = clienteRepositoryAdapter.save(cliente);

        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        verify(clienteJpaRepository, times(1)).save(cliente);
    }

    @Test
    @DisplayName("Debe obtener cliente por ID mediante JPA")
    void testFindById() {
        when(clienteJpaRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteRepositoryAdapter.findById(1L);

        assertTrue(resultado.isPresent());
        verify(clienteJpaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe obtener cliente por ClienteId mediante JPA")
    void testFindByClienteId() {
        when(clienteJpaRepository.findByClienteId("CLIENTE001")).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteRepositoryAdapter.findByClienteId("CLIENTE001");

        assertTrue(resultado.isPresent());
        verify(clienteJpaRepository, times(1)).findByClienteId("CLIENTE001");
    }

    @Test
    @DisplayName("Debe obtener todos los clientes mediante JPA")
    void testFindAll() {
        List<Cliente> clientes = List.of(cliente);
        when(clienteJpaRepository.findAll()).thenReturn(clientes);

        List<Cliente> resultado = clienteRepositoryAdapter.findAll();

        assertEquals(1, resultado.size());
        verify(clienteJpaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe actualizar cliente mediante JPA")
    void testUpdate() {
        clienteRepositoryAdapter.update(cliente);

        verify(clienteJpaRepository, times(1)).save(cliente);
    }

    @Test
    @DisplayName("Debe eliminar cliente por ID mediante JPA")
    void testDeleteById() {
        clienteRepositoryAdapter.deleteById(1L);

        verify(clienteJpaRepository, times(1)).deleteById(1L);
    }
}
