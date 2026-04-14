package servicio;

import com.novabank.modelo.Cliente;
import com.novabank.modelo.Cuenta;
import com.novabank.repositorio.CuentaDAO;
import com.novabank.servicio.ClienteService;
import com.novabank.servicio.CuentaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Activamos Mockito
@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    // Mock DAO
    @Mock
    private CuentaDAO cuentaDAOMock;

    @Mock
    private ClienteService clienteServiceMock;

    // Inyectamos los mocks en nuestro servicio principal
    @InjectMocks
    private CuentaService cuentaService;

    @Test
    void crearCuenta_conClienteExistente_debeGenerarYGuardarCuenta() {
        // Preparamos
        Long idCliente = 1L;
        Cliente clienteSimulado = new Cliente("Juan", "Perez", "12345678A", "juan@email.com", "600123123");
        clienteSimulado.setId(idCliente);

        // Simulamos que el ClienteService encuentra al cliente
        when(clienteServiceMock.buscarPorId(idCliente)).thenReturn(clienteSimulado);

        // ¡Simulamos que la base de datos devuelve '1' como siguiente número secuencial
        when(cuentaDAOMock.obtenerSiguienteNumeroSecuencial()).thenReturn(1L);

        // Simulamos que al guardar en BD, devuelve la misma cuenta que le pasamos
        when(cuentaDAOMock.guardar(any(Cuenta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecutamos
        Cuenta nuevaCuenta = cuentaService.crearCuenta(idCliente);

        // Comprobamos
        assertNotNull(nuevaCuenta);
        assertEquals(idCliente, nuevaCuenta.getClienteId());
        assertEquals("ES91210000000000000001", nuevaCuenta.getNumeroCuenta());

        assertEquals(BigDecimal.ZERO, nuevaCuenta.getSaldo());

        // Verificamos que el DAO ha guardado la cuenta exactamente 1 vez
        verify(cuentaDAOMock, times(1)).guardar(any(Cuenta.class));
    }

    @Test
    void crearCuenta_conClienteInexistente_debeLanzarExcepcion() {
        // Simulamos que no encuentra al cliente
        when(clienteServiceMock.buscarPorId(9999L)).thenReturn(null);

        // Verificamos que lanza la excepción
        assertThrows(IllegalArgumentException.class, () -> {
            cuentaService.crearCuenta(9999L);
        });

        // Verificamos que nunca se llamó al DAO para guardar la cuenta
        verify(cuentaDAOMock, never()).guardar(any(Cuenta.class));
    }

    @Test
    void listarCuentasDeCliente_debeDevolverSoloCuentasDelCliente() {
        Long idCliente = 1L;

        // Preparamos dos cuentas de prueba
        Cuenta cuenta1 = new Cuenta("ES91210000000000000001", idCliente);
        Cuenta cuenta2 = new Cuenta("ES91210000000000000002", idCliente);

        // Simulamos que al buscar en la base de datos, devuelve nuestra lista prefabricada
        when(cuentaDAOMock.listarPorCliente(idCliente)).thenReturn(Arrays.asList(cuenta1, cuenta2));

        // Ejecutamos
        List<Cuenta> cuentasDeAna = cuentaService.listarCuentasDeCliente(idCliente);

        // Comprobamos
        assertEquals(2, cuentasDeAna.size());

        // Verificamos que se llamó al método correcto del DAO
        verify(cuentaDAOMock, times(1)).listarPorCliente(idCliente);
    }
}