package servicio;

import com.novabank.modelo.Cliente;
import com.novabank.repositorio.Memoria;
import com.novabank.servicio.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Activamos Mockito
@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    // Mockito lo "espía" para dejarnos verificar cuántas veces se llaman a sus métodos.
    @Spy
    private Memoria memoriaSpy = new Memoria();

    // Inyectamos la memoria espiada en el servicio de clientes
    @InjectMocks
    private ClienteService clienteService;

    @Test
    void crearCliente_conDatosCorrectos_debeGuardarCliente() {
        // Ejecutamos la acción
        Cliente nuevo = clienteService.crearCliente("Juan", "Perez", "12345678A", "juan@email.com", "600123123");

        // Comprobamos los resultados
        assertNotNull(nuevo);
        assertEquals("Juan", nuevo.getNombre());
        assertEquals("12345678A", nuevo.getDni());
        assertEquals(1, memoriaSpy.clientes.size()); // Comprobamos el tamaño de la lista

        // Verificamos que el método guardarCliente se llamó 1 vez
        verify(memoriaSpy, times(1)).guardarCliente(any(Cliente.class));
    }

    @Test
    void crearCliente_conDniRepetido_debeLanzarExcepcion() {
        // Metemos un cliente en la memoria espía para simular que ya existe
        Cliente clienteExistente = new Cliente("Ana", "Gomez", "87654321B", "ana@email.com", "600111222");
        memoriaSpy.clientes.put(1L, clienteExistente);

        // Intentamos crear otro cliente con el mismo DNI
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Pedro", "Ruiz", "87654321B", "pedro@email.com", "600333444");
        });

        // Verificamos que al saltar la excepción, no se guardó el nuevo cliente
        verify(memoriaSpy, never()).guardarCliente(any(Cliente.class));
    }

    @Test
    void crearCliente_sinNombre_debeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("", "Perez", "12345678A", "juan@email.com", "600123123");
        });
        verify(memoriaSpy, never()).guardarCliente(any(Cliente.class));
    }

    @Test
    void crearCliente_sinApellidos_debeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Juan", "", "12345678A", "juan@email.com", "600123123");
        });
        verify(memoriaSpy, never()).guardarCliente(any(Cliente.class));
    }

    @Test
    void crearCliente_sinDni_debeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Juan", "Perez", "", "juan@email.com", "600123123");
        });
        verify(memoriaSpy, never()).guardarCliente(any(Cliente.class));
    }

    @Test
    void crearCliente_sinEmail_debeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Juan", "Perez", "12345678A", "", "600123123");
        });
        verify(memoriaSpy, never()).guardarCliente(any(Cliente.class));
    }

    @Test
    void crearCliente_sinTelefono_debeLanzarExcepcion() {
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Juan", "Perez", "12345678A", "juan@email.com", "");
        });
        verify(memoriaSpy, never()).guardarCliente(any(Cliente.class));
    }
}