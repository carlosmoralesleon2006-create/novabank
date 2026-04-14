package servicio;

import com.novabank.modelo.Cuenta;
import com.novabank.modelo.Movimiento;
import com.novabank.repositorio.OperacionDAO;
import com.novabank.servicio.CuentaService;
import com.novabank.servicio.OperacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Activamos mockito
@ExtendWith(MockitoExtension.class)
class OperacionServiceTest {

    @Mock
    private OperacionDAO operacionDAOMock;

    @Mock
    private CuentaService cuentaServiceMock;

    @InjectMocks
    private OperacionService operacionService;

    private Cuenta cuentaOrigen;
    private Cuenta cuentaDestino;

    @BeforeEach
    void setUp() {
        // Preparamos unas cuentas de prueba que usaremos en los tests
        cuentaOrigen = new Cuenta("ES91210000000000000001", 1000L);
        cuentaOrigen.setSaldo(new BigDecimal("1000")); // Le damos 1000€ iniciales

        cuentaDestino = new Cuenta("ES91210000000000000002", 1001L);
        cuentaDestino.setSaldo(BigDecimal.ZERO);
    }

    @Test
    void depositar_conImportePositivo_debeActualizarSaldo() {
        // Le decimos al mock cómo debe comportarse.
        when(cuentaServiceMock.buscarPorNumero("ES91210000000000000001")).thenReturn(Optional.ofNullable(cuentaOrigen));

        // Hacemos un depósito de 500€
        operacionService.depositar("ES91210000000000000001", new BigDecimal("500"));

        // Comprobamos
        assertEquals(new BigDecimal("1500"), cuentaOrigen.getSaldo());

        // Verificamos que se actualizó el saldo en la base de datos
        verify(cuentaServiceMock, times(1)).actualizar(cuentaOrigen);

        // Verificamos que se haya llamado al método guardarMovimiento en el DAO
        verify(operacionDAOMock, times(1)).guardarMovimiento(any(Movimiento.class));
    }

    @Test
    void retirar_conSaldoInsuficiente_debeLanzarExcepcion() {
        // Configuramos el mock para que devuelva la cuenta
        when(cuentaServiceMock.buscarPorNumero("ES91210000000000000001")).thenReturn(Optional.ofNullable(cuentaOrigen));

        // Intentamos retirar 5000€
        assertThrows(IllegalArgumentException.class, () -> {
            operacionService.retirar("ES91210000000000000001", new BigDecimal("5000"));
        });

        // Verificamos que no se actualizó la BD ni se guardó movimiento
        verify(cuentaServiceMock, never()).actualizar(any());
        verify(operacionDAOMock, never()).guardarMovimiento(any(Movimiento.class));
    }

    @Test
    void transferir_conSaldoSuficiente_debeActualizarAmbasCuentas() {
        // Configuramos el mock para que devuelva ambas cuentas
        when(cuentaServiceMock.buscarPorNumero("ES91210000000000000001")).thenReturn(Optional.ofNullable(cuentaOrigen));
        when(cuentaServiceMock.buscarPorNumero("ES91210000000000000002")).thenReturn(Optional.ofNullable(cuentaDestino));

        // Transferimos 300€ del origen al destino
        operacionService.transferir("ES91210000000000000001", "ES91210000000000000002", new BigDecimal("300"));

        // El origen se queda con 700€ y el destino con 300€
        assertEquals(new BigDecimal("700"), cuentaOrigen.getSaldo());
        assertEquals(new BigDecimal("300"), cuentaDestino.getSaldo());

        // Verificamos que se actualizaron ambas cuentas en la BD
        verify(cuentaServiceMock, times(1)).actualizar(cuentaOrigen);
        verify(cuentaServiceMock, times(1)).actualizar(cuentaDestino);

        // Se deben haber guardado 2 movimientos
        verify(operacionDAOMock, times(2)).guardarMovimiento(any(Movimiento.class));
    }
}