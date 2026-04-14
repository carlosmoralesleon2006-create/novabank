package servicio;

import com.novabank.modelo.Cliente;
import com.novabank.repositorio.ClienteDAO;
import com.novabank.servicio.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Activamos Mockito
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private ClienteDAO clienteDAOMock;

    // Inyectamos el DAO en el servicio
    @InjectMocks
    private ClienteService clienteService;

    @Test
    void crearCliente_conDatosCorrectos_debeGuardarCliente() {
        Cliente clienteSimulado = new Cliente.ClienteBuilder()
                .conNombre("Juan")
                .conApellidos("Perez")
                .conDni("12345678A")
                .conEmail("juan@email.com")
                .conTelefono("600123123")
                .build();
        clienteSimulado.setId(1L);
        when(clienteDAOMock.guardar(any(Cliente.class))).thenReturn(clienteSimulado);

        Cliente nuevo = clienteService.crearCliente("Juan", "Perez", "12345678A", "juan@email.com", "600123123");

        assertNotNull(nuevo);
        assertEquals("Juan", nuevo.getNombre());
        assertEquals("12345678A", nuevo.getDni());
        assertEquals(1L, nuevo.getId()); // Comprobamos que recibió el ID generado

        // Verificamos que el método guardar del DAO se llamó 1 vez
        verify(clienteDAOMock, times(1)).guardar(any(Cliente.class));
    }

    @Test
    void crearCliente_conDniRepetido_debeLanzarExcepcion() {
        Cliente clienteExistente = new Cliente.ClienteBuilder()
                .conNombre("Ana")
                .conApellidos("Gomez")
                .conDni("87654321B")
                .conEmail("ana@email.com")
                .conTelefono("600111222")
                .build();

        when(clienteDAOMock.buscarPorDni("87654321B")).thenReturn(Optional.of(clienteExistente));

        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Pedro", "Ruiz", "87654321B", "pedro@email.com", "600333444");
        });

        verify(clienteDAOMock, never()).guardar(any(Cliente.class));
    }

    @Test
    void crearCliente_sinNombre_debeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("", "Perez", "12345678A", "juan@email.com", "600123123");
        });
        verify(clienteDAOMock, never()).guardar(any(Cliente.class));
    }

    @Test
    void crearCliente_sinApellidos_debeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Juan", "", "12345678A", "juan@email.com", "600123123");
        });
        verify(clienteDAOMock, never()).guardar(any(Cliente.class));
    }

    @Test
    void crearCliente_sinDni_debeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Juan", "Perez", "", "juan@email.com", "600123123");
        });
        verify(clienteDAOMock, never()).guardar(any(Cliente.class));
    }

    @Test
    void crearCliente_sinEmail_debeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Juan", "Perez", "12345678A", "", "600123123");
        });
        verify(clienteDAOMock, never()).guardar(any(Cliente.class));
    }

    @Test
    void crearCliente_sinTelefono_debeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Juan", "Perez", "12345678A", "juan@email.com", "");
        });
        verify(clienteDAOMock, never()).guardar(any(Cliente.class));
    }
}