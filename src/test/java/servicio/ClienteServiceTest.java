package servicio;

import com.novabank.modelo.Cliente;
import com.novabank.repositorio.ClienteDAO;
import com.novabank.servicio.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Activamos Mockito
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    // Así no necesitamos que la base de datos PostgreSQL esté encendida para pasar los tests.
    @Mock
    private ClienteDAO clienteDAOMock;

    // Inyectamos el DAO falso en nuestro servicio
    @InjectMocks
    private ClienteService clienteService;

    @Test
    void crearCliente_conDatosCorrectos_debeGuardarCliente() {
        // Le decimos al mock cómo debe comportarse cuando le pidan guardar un cliente.
        // Simulamos que la base de datos lo guarda y le asigna el ID 1.
        Cliente clienteSimulado = new Cliente("Juan", "Perez", "12345678A", "juan@email.com", "600123123");
        clienteSimulado.setId(1L);
        when(clienteDAOMock.guardar(any(Cliente.class))).thenReturn(clienteSimulado);

        // Ejecutamos la acción en el servicio
        Cliente nuevo = clienteService.crearCliente("Juan", "Perez", "12345678A", "juan@email.com", "600123123");

        // Comprobamos los resultados
        assertNotNull(nuevo);
        assertEquals("Juan", nuevo.getNombre());
        assertEquals("12345678A", nuevo.getDni());
        assertEquals(1L, nuevo.getId()); // Comprobamos que recibió el ID generado

        // Verificamos que el método guardar del DAO se llamó 1 vez
        verify(clienteDAOMock, times(1)).guardar(any(Cliente.class));
    }

    @Test
    void crearCliente_conDniRepetido_debeLanzarExcepcion() {
        // Simulamos que la base de datos ya tiene un cliente registrado al buscar duplicados.
        Cliente clienteExistente = new Cliente("Ana", "Gomez", "87654321B", "ana@email.com", "600111222");
        when(clienteDAOMock.listarTodos()).thenReturn(List.of(clienteExistente));

        // Intentamos crear otro cliente con el mismo DNI
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Pedro", "Ruiz", "87654321B", "pedro@email.com", "600333444");
        });

        // Verificamos que al saltar la excepción, no se intentó guardar en la base de datos
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