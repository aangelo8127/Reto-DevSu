package org.devsu.com.clientepersona.application.usecase;

import org.devsu.com.clientepersona.domain.entity.Cliente;
import org.devsu.com.clientepersona.domain.port.ClienteEventPublisher;
import org.devsu.com.clientepersona.domain.port.ClienteRepository;
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
@DisplayName("ClienteService Tests")
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteEventPublisher clienteEventPublisher;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente cliente;

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
    }

    @Test
    @DisplayName("Debe crear un cliente exitosamente")
    void testCrearCliente() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        Cliente resultado = clienteService.crearCliente(cliente);

        assertNotNull(resultado);
        assertEquals("Juan Pérez", resultado.getNombre());
        assertEquals("CLIENTE001", resultado.getClienteId());
        verify(clienteRepository, times(1)).save(cliente);
        verify(clienteEventPublisher, times(1)).publishClienteCreated(cliente);
    }

    @Test
    @DisplayName("Debe obtener cliente por ID")
    void testObtenerClientePorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.obtenerClientePorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Juan Pérez", resultado.get().getNombre());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Debe retornar vacío cuando cliente no existe por ID")
    void testObtenerClientePorIdNoExiste() {
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        Optional<Cliente> resultado = clienteService.obtenerClientePorId(999L);

        assertFalse(resultado.isPresent());
        verify(clienteRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("Debe obtener cliente por ClienteId")
    void testObtenerClientePorClienteId() {
        when(clienteRepository.findByClienteId("CLIENTE001")).thenReturn(Optional.of(cliente));

        Optional<Cliente> resultado = clienteService.obtenerClientePorClienteId("CLIENTE001");

        assertTrue(resultado.isPresent());
        assertEquals("CLIENTE001", resultado.get().getClienteId());
        verify(clienteRepository, times(1)).findByClienteId("CLIENTE001");
    }

    @Test
    @DisplayName("Debe obtener todos los clientes")
    void testObtenerTodosLosClientes() {
        Cliente cliente2 = new Cliente();
        cliente2.setPersonaId(2L);
        cliente2.setNombre("María García");
        cliente2.setClienteId("CLIENTE002");

        List<Cliente> clientes = List.of(cliente, cliente2);
        when(clienteRepository.findAll()).thenReturn(clientes);

        List<Cliente> resultado = clienteService.obtenerTodosLosClientes();

        assertEquals(2, resultado.size());
        verify(clienteRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Debe actualizar cliente exitosamente")
    void testActualizarCliente() {
        cliente.setNombre("Juan Pérez Actualizado");

        clienteService.actualizarCliente(cliente);

        verify(clienteRepository, times(1)).update(cliente);
        verify(clienteEventPublisher, times(1)).publishClienteUpdated(cliente);
    }

    @Test
    @DisplayName("Debe eliminar cliente exitosamente")
    void testEliminarCliente() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        clienteService.eliminarCliente(1L);

        verify(clienteRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).deleteById(1L);
        verify(clienteEventPublisher, times(1)).publishClienteDeleted("CLIENTE001");
    }

    @Test
    @DisplayName("No debe eliminar si cliente no existe")
    void testEliminarClienteNoExiste() {
        when(clienteRepository.findById(999L)).thenReturn(Optional.empty());

        clienteService.eliminarCliente(999L);

        verify(clienteRepository, times(1)).findById(999L);
        verify(clienteRepository, never()).deleteById(999L);
        verify(clienteEventPublisher, never()).publishClienteDeleted(anyString());
    }
}
