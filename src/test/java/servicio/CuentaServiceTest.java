package servicio;

import com.novabank.modelo.Cliente;
import com.novabank.modelo.Cuenta;
import com.novabank.repositorio.Memoria;
import com.novabank.servicio.ClienteService;
import com.novabank.servicio.CuentaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Activamos Mockito en esta clase
@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    // Mockito lo "espía" para dejarnos verificar cuántas veces se llaman a sus métodos.
    @Spy
    private Memoria memoriaSpy = new Memoria();

    // Lo aislamos para no depender de la creación real de clientes.
    @Mock
    private ClienteService clienteServiceMock;

    // Inyectamos los dobles en nuestro servicio principal
    @InjectMocks
    private CuentaService cuentaService;

    @Test
    void crearCuenta_conClienteExistente_debeGenerarYGuardarCuenta() {
        // Preparamos
        Long idCliente = 1L;
        // Creamos un cliente solo para que el mock lo devuelva
        Cliente clienteSimulado = new Cliente("Juan", "Perez", "12345678A", "juan@email.com", "600123123");
        clienteSimulado.setId(idCliente);

        // Cuando el CuentaService pregunte por el ID 1, devuelve el clienteSimulado
        when(clienteServiceMock.buscarPorId(idCliente)).thenReturn(clienteSimulado);

        // Ejecutamos
        Cuenta nuevaCuenta = cuentaService.crearCuenta(idCliente);

        // Comprobamos
        assertNotNull(nuevaCuenta);
        assertEquals(idCliente, nuevaCuenta.getClienteId());
        assertEquals("ES91210000000000000001", nuevaCuenta.getNumeroCuenta());
        assertEquals(BigDecimal.ZERO, nuevaCuenta.getSaldo());

        // Verificamos que la memoria ha guardado la cuenta exactamente 1 vez
        verify(memoriaSpy, times(1)).guardarCuenta(any(Cuenta.class));
    }

    @Test
    void crearCuenta_conClienteInexistente_debeLanzarExcepcion() {
        // Le decimos al mock que devuelva null como si no hubiera encontrado al cliente
        when(clienteServiceMock.buscarPorId(9999L)).thenReturn(null);

        // Verificamos que lanza la excepción
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaService.crearCuenta(9999L);
        });

        // Verificamos que NUNCA se llegó a llamar al método guardarCuenta
        verify(memoriaSpy, never()).guardarCuenta(any(Cuenta.class));
    }

    @Test
    void listarCuentasDeCliente_debeDevolverSoloCuentasDelCliente() {
        Long idCliente = 1L;

        // insertamos dos cuentas directamente en la memoria espía para simular que ya existen.
        Cuenta cuenta1 = new Cuenta("ES91210000000000000001", idCliente);
        Cuenta cuenta2 = new Cuenta("ES91210000000000000002", idCliente);
        memoriaSpy.cuentas.put(cuenta1.getNumeroCuenta(), cuenta1);
        memoriaSpy.cuentas.put(cuenta2.getNumeroCuenta(), cuenta2);

        List<Cuenta> cuentasDeAna = cuentaService.listarCuentasDeCliente(idCliente);

        assertEquals(2, cuentasDeAna.size());
    }
}