package servicio;


import com.novabank.modelo.Cuenta;
import com.novabank.repositorio.Memoria;
import com.novabank.servicio.ClienteService;
import com.novabank.servicio.CuentaService;
import com.novabank.servicio.OperacionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class OperacionServiceTest {

    private Memoria memoria;
    private OperacionService operacionService;
    private Cuenta cuentaPrueba;

    @BeforeEach
    void setUp() {
        memoria = new Memoria();
        CuentaService cuentaService = new CuentaService(memoria, new ClienteService(memoria));
        operacionService = new OperacionService(memoria, cuentaService);

        // Preparamos una cuenta de prueba en la memoria para poder hacerle operaciones
        cuentaPrueba = new Cuenta("ES91210000000000000001", 1000L);
        memoria.guardarCuenta(cuentaPrueba);
    }

    @Test
    void depositar_conImportePositivo_debeActualizarSaldo() {
        // Ingresamos 500 euros
        operacionService.depositar("ES91210000000000000001", new BigDecimal("500"));

        // Comprobamos que el saldo ahora es 500
        assertEquals(new BigDecimal("500"), cuentaPrueba.getSaldo());
    }

    @Test
    void retirar_conSaldoInsuficiente_debeLanzarExcepcion() {
        // La cuenta empieza con saldo 0. Intentamos retirar 100 euros.
        assertThrows(IllegalArgumentException.class, () -> {
            operacionService.retirar("ES91210000000000000001", new BigDecimal("100"));
        });

        // Comprobamos que el saldo no cambió (sigue siendo 0)
        assertEquals(BigDecimal.ZERO, cuentaPrueba.getSaldo());
    }

    @Test
    void transferir_conSaldoSuficiente_debeActualizarAmbasCuentas() {
        // Preparamos una segunda cuenta de destino
        Cuenta cuentaDestino = new Cuenta("ES91210000000000000002", 1001L);
        memoria.guardarCuenta(cuentaDestino);

        // Le damos 1000 euros de saldo inicial a la cuenta origen a mano
        cuentaPrueba.setSaldo(new BigDecimal("1000"));

        // Hacemos la transferencia de 300 euros
        operacionService.transferir("ES91210000000000000001", "ES91210000000000000002", new BigDecimal("300"));

        // Comprobamos los saldos (Origen: 1000 - 300 = 700. Destino: 0 + 300 = 300)
        assertEquals(new BigDecimal("700"), cuentaPrueba.getSaldo());
        assertEquals(new BigDecimal("300"), cuentaDestino.getSaldo());
    }
}