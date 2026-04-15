package repositorio;

import com.novabank.modelo.Cliente;
import com.novabank.modelo.Cuenta;
import com.novabank.modelo.Movimiento;
import com.novabank.modelo.MovimientoFactory;
import com.novabank.repositorio.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OperacionDAOTest {

    private OperacionDAO operacionDAO;
    private CuentaDAO cuentaDAO;
    private ClienteDAO clienteDAO;

    @BeforeEach
    void setUp() {
        operacionDAO = new OperacionDAOimpl();
        cuentaDAO = new CuentaDAOimpl();
        clienteDAO = new ClienteDAOimpl();
    }

    @Test
    void guardarYListarMovimientos_enBaseDeDatosReal_debeFuncionar() {
        String idUnico = String.valueOf(System.currentTimeMillis()).substring(7);
        Cliente clienteTest = new Cliente.ClienteBuilder()
                .conNombre("ClienteMov")
                .conApellidos("Integracion")
                .conDni("30" + idUnico + "Z")
                .conEmail("mov" + idUnico + "@email.com")
                .conTelefono("800" + idUnico)
                .build();
        clienteDAO.guardar(clienteTest);

        String numeroCuentaTest = "ES91999900000000" + idUnico;
        Cuenta cuentaTest = new Cuenta(numeroCuentaTest, clienteTest.getId());
        cuentaTest.setSaldo(new BigDecimal("1000.00"));
        cuentaTest.setFechaCreacion(LocalDateTime.now());
        cuentaDAO.guardar(cuentaTest);

        Movimiento deposito = MovimientoFactory.crearDeposito(numeroCuentaTest, new BigDecimal("250.00"));

        try (Connection conn = ConexionDB.obtenerConexion()) {
            Movimiento movGuardado = operacionDAO.guardarMovimiento(deposito, conn);

            assertNotNull(movGuardado.getId(), "El ID no debería ser nulo tras guardar en BD");
            assertNotNull(movGuardado.getFecha(), "La fecha no debería ser nula tras guardar en BD");
        } catch (Exception e) {
            fail("El test falló por un error de SQL o conexión: " + e.getMessage());
        }

        List<Movimiento> historial = operacionDAO.obtenerHistorialPorCuenta(numeroCuentaTest);

        assertFalse(historial.isEmpty());
        assertEquals(1, historial.size());
        assertEquals(new BigDecimal("250.00"), historial.get(0).getCantidad());
        assertEquals("DEPOSITO", historial.get(0).getTipo().name());
    }
}