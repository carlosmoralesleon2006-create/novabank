package servicio;

import com.novabank.modelo.Cliente;
import com.novabank.repositorio.Memoria;
import com.novabank.servicio.ClienteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClienteServiceTest {

    private Memoria memoria;
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        memoria = new Memoria();
        clienteService = new ClienteService(memoria);
    }

    @Test
    void crearCliente_conDatosCorrectos_debeGuardarCliente() {
        //Creamos un cliente normal
        Cliente nuevo = clienteService.crearCliente("Juan", "Perez", "12345678A", "juan@email.com", "600123123");

        // Comprobamos los resultados
        assertNotNull(nuevo);
        assertEquals("Juan", nuevo.getNombre());
        assertEquals("12345678A", nuevo.getDni());
        assertEquals(1, memoria.clientes.size()); // Compruebamos que se guardó el cliente en memoria
    }

    @Test
    void crearCliente_conDniRepetido_debeLanzarExcepcion() {
        // Primero creamos un cliente normal
        clienteService.crearCliente("Ana", "Gomez", "87654321B", "ana@email.com", "600111222");

        // Intentamos crear otro cliente con el mismo DNI y comprobamos si lanza un error
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Pedro", "Ruiz", "87654321B", "pedro@email.com", "600333444");
        });
    }

    @Test
    void crearCliente_sinNombre_debeLanzarExcepcion() {
        // Intentamos crear un cliente dejando el nombre vacío
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("", "Perez", "12345678A", "juan@email.com", "600123123");
        });
    }
    @Test
    void crearCliente_sinApellidos_debeLanzarExcepcion() {
        // Intentamos crear un cliente dejando los apellidos vacíos
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Juan", "", "12345678A", "juan@email.com", "600123123");
        });
    }
    @Test
    void crearCliente_sinDni_debeLanzarExcepcion() {
        // Intentamos crear un cliente dejando el dni vacío
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Juan", "Perez", "", "juan@email.com", "600123123");
        });
    }
    @Test
    void crearCliente_sinEmail_debeLanzarExcepcion() {
        // Intentamos crear un cliente dejando el email vacío
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Juan", "Perez", "12345678A", "", "600123123");
        });
    }
    @Test
    void crearCliente_sinTelefono_debeLanzarExcepcion() {
        // Intentamos crear un cliente dejando el telefono vacío
        assertThrows(IllegalArgumentException.class, () -> {
            clienteService.crearCliente("Juan", "Perez", "12345678A", "juan@email.com", "");
        });
    }
}