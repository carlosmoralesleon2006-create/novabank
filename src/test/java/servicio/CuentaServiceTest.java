package servicio;


import com.novabank.modelo.Cliente;
import com.novabank.modelo.Cuenta;
import com.novabank.repositorio.Memoria;
import com.novabank.servicio.ClienteService;
import com.novabank.servicio.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CuentaServiceTest {

    private Memoria memoria;
    private CuentaService cuentaService;
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        memoria = new Memoria();
        clienteService = new ClienteService(memoria);
        cuentaService = new CuentaService(memoria, clienteService);
    }

    @Test
    void crearCuenta_conClienteExistente_debeGenerarYGuardarCuenta() {
        //Creamos un cliente y guardamos su id para uso posterior
        Cliente cliente = clienteService.crearCliente("Juan", "Perez", "12345678A", "juan@email.com", "600123123");
        Long idCliente = cliente.getId();

        //Creamos una cuenta nueva con el identificador del cliente creado
        Cuenta nuevaCuenta = cuentaService.crearCuenta(idCliente);

        //Comprobamos que la cuenta no se nula
        assertNotNull(nuevaCuenta);

        //Comprobamos que el identificador del cliente de la cuenta es el mismo que el del cliente proporcionado a la misma
        assertEquals(idCliente, nuevaCuenta.getClienteId());

        //El número de la cuenta debe ser el 1 ya que es la primera cuenta almacenada
        assertEquals("ES91210000000000000001", nuevaCuenta.getNumeroCuenta());

        //Comprobamos que el saldo incial es cero
        assertEquals(BigDecimal.ZERO, nuevaCuenta.getSaldo());

        //Comprobamos que se ha guardado en la memoria
        assertEquals(1, memoria.cuentas.size());
    }

    @Test
    void crearCuenta_conClienteInexistente_debeLanzarExcepcion() {
        //Verificamos que da error al crear una cuenta si un cliente existente
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaService.crearCuenta(9999L);
        });
    }

    @Test
    void listarCuentasDeCliente_debeDevolverSoloCuentasDelCliente() {
        //Verificamos que se devuelven solo las cuentas del cliente indicado
        Cliente cliente = clienteService.crearCliente("Ana", "Gomez", "87654321B", "ana@email.com", "600111222");
        Long idCliente = cliente.getId();

        //Creamos dos cuentas pra el mismo cliente
        cuentaService.crearCuenta(idCliente);
        cuentaService.crearCuenta(idCliente);

        //Creamos una lista con las cuentas devueltas por el método de listado por id
        List<Cuenta> cuentasDeAna = cuentaService.listarCuentasDeCliente(idCliente);

        //Verificamos si realmente se han devuelto las dos cuentas creadas
        assertEquals(2, cuentasDeAna.size());
    }
}