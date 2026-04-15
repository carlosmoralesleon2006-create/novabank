package repositorio;

import com.novabank.modelo.Cliente;
import com.novabank.modelo.Cuenta;
import com.novabank.repositorio.ClienteDAO;
import com.novabank.repositorio.ClienteDAOimpl;
import com.novabank.repositorio.CuentaDAO;
import com.novabank.repositorio.CuentaDAOimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CuentaDAOTest {

    private CuentaDAO cuentaDAO;
    private ClienteDAO clienteDAO;

    @BeforeEach
    void setUp() {
        cuentaDAO = new CuentaDAOimpl();
        clienteDAO = new ClienteDAOimpl();
    }

    @Test
    void guardarYBuscarCuenta_enBaseDeDatosReal_debeFuncionar() {
        String idUnico = String.valueOf(System.currentTimeMillis()).substring(7);
        Cliente clienteTest = new Cliente.ClienteBuilder()
                .conNombre("ClienteCuenta")
                .conApellidos("Integracion")
                .conDni("20" + idUnico + "Y")
                .conEmail("cuenta" + idUnico + "@email.com")
                .conTelefono("700" + idUnico)
                .build();

        Cliente clienteGuardado = clienteDAO.guardar(clienteTest);

        String numeroCuentaTest = "ES91000000000000" + idUnico;
        Cuenta nuevaCuenta = new Cuenta(numeroCuentaTest, clienteGuardado.getId());
        nuevaCuenta.setSaldo(new BigDecimal("500.00"));
        nuevaCuenta.setFechaCreacion(LocalDateTime.now());

        Cuenta cuentaGuardada = cuentaDAO.guardar(nuevaCuenta);

        assertNotNull(cuentaGuardada.getId());

        Optional<Cuenta> cuentaEncontrada = cuentaDAO.buscarPorNumeroDeCuenta(numeroCuentaTest);

        assertTrue(cuentaEncontrada.isPresent());
        assertEquals(new BigDecimal("500.00"), cuentaEncontrada.get().getSaldo());
        assertEquals(clienteGuardado.getId(), cuentaEncontrada.get().getClienteId());
    }
}