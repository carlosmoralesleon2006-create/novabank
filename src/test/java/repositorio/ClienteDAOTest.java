package repositorio;

import com.novabank.modelo.Cliente;
import com.novabank.repositorio.ClienteDAO;
import com.novabank.repositorio.ClienteDAOimpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ClienteDAOTest {

    private ClienteDAO clienteDAO;

    @BeforeEach
    void setUp() {
        clienteDAO = new ClienteDAOimpl();
    }

    @Test
    void guardarYBuscarCliente_enBaseDeDatosReal_debeFuncionar() {
        String idUnico = String.valueOf(System.currentTimeMillis()).substring(7);
        String dniTest = "10" + idUnico + "X";
        String emailTest = "test" + idUnico + "@email.com";

        Cliente nuevoCliente = new Cliente.ClienteBuilder()
                .conNombre("TestIntegracion")
                .conApellidos("Prueba Real")
                .conDni(dniTest)
                .conEmail(emailTest)
                .conTelefono("600" + idUnico)
                .build();

        Cliente clienteGuardado = clienteDAO.guardar(nuevoCliente);

        assertNotNull(clienteGuardado.getId(), "PostgreSQL debería haber generado un ID");
        assertTrue(clienteGuardado.getId() > 0);

        Optional<Cliente> clienteEncontrado = clienteDAO.buscarPorDni(dniTest);

        assertTrue(clienteEncontrado.isPresent());
        assertEquals("TestIntegracion", clienteEncontrado.get().getNombre());
        assertEquals(emailTest, clienteEncontrado.get().getEmail());
    }
}