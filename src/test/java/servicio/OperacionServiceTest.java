package servicio;

import com.novabank.modelo.Cuenta;
import com.novabank.modelo.Movimiento;
import com.novabank.repositorio.Memoria;
import com.novabank.servicio.CuentaService;
import com.novabank.servicio.OperacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Le indicamos a JUnit que vamos a usar mockito
@ExtendWith(MockitoExtension.class)
class OperacionServiceTest {

    //@Mock crea "copias" de las dependencias sin necesidad de ejecutar las reales
    @Mock
    private Memoria memoriaMock;

    @Mock
    private CuentaService cuentaServiceMock;

    // @InjectMocks crea una instancia real de la clase que queremos probar,
    // pero le inyecta las "copias" que creamos arriba en lugar de las clases reales.
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
        // Si alguien llama al método "buscarPorNumero" con este IBAN se devolverá la cuenta de origen
        when(cuentaServiceMock.buscarPorNumero("ES91210000000000000001")).thenReturn(cuentaOrigen);

        // Hacemos un depósito de 500€
        operacionService.depositar("ES91210000000000000001", new BigDecimal("500"));

        // Comprobamos
        // El saldo debe haber sumado 500 (1000 + 500 = 1500)
        assertEquals(new BigDecimal("1500"), cuentaOrigen.getSaldo());
        // Verificamos que se haya llamado al método guardarMovimiento exactamente 1 vez
        verify(memoriaMock, times(1)).guardarMovimiento(any(Movimiento.class));
    }

    @Test
    void retirar_conSaldoInsuficiente_debeLanzarExcepcion() {
        // Configuramos el mock para que devuelva la cuenta
        when(cuentaServiceMock.buscarPorNumero("ES91210000000000000001")).thenReturn(cuentaOrigen);

        // Intentamos retirar 5000€ (solo tiene 1000€)
        assertThrows(IllegalArgumentException.class, () -> {
            operacionService.retirar("ES91210000000000000001", new BigDecimal("5000"));
        });

        // Verificamos que, como dio error, no se guardó un movimiento
        verify(memoriaMock, never()).guardarMovimiento(any(Movimiento.class));
    }

    @Test
    void transferir_conSaldoSuficiente_debeActualizarAmbasCuentas() {
        // Configuramos el mock para que devuelva ambas cuentas
        when(cuentaServiceMock.buscarPorNumero("ES91210000000000000001")).thenReturn(cuentaOrigen);
        when(cuentaServiceMock.buscarPorNumero("ES91210000000000000002")).thenReturn(cuentaDestino);

        // Transferimos 300€ del origen al destino
        operacionService.transferir("ES91210000000000000001", "ES91210000000000000002", new BigDecimal("300"));

        // El origen se queda con 700€ y el destino con 300€
        assertEquals(new BigDecimal("700"), cuentaOrigen.getSaldo());
        assertEquals(new BigDecimal("300"), cuentaDestino.getSaldo());

        // Se deben haber guardado 2 movimientos (el de salida y el de entrada)
        verify(memoriaMock, times(2)).guardarMovimiento(any(Movimiento.class));
    }
}