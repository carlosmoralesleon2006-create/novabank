package com.novabank.repositorio;

import com.novabank.modelo.Cliente;

import javax.swing.text.html.Option;
import javax.tools.OptionChecker;
import java.lang.ref.Cleaner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteDAOimpl implements ClienteDAO {

    public ClienteDAOimpl(){}

    // METODO PARA GUARDAR UN CLIENTE NUEVO EN LA BASE DE DATOS
    @Override
    public Cliente guardar(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, apellidos, dni, email, telefono) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
             // Le decimos a Java que queremos recuperar el ID que PostgreSQL genera automáticamente
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Rellenamos los interrogantes con los datos del cliente proporcionado
            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getApellidos());
            pstmt.setString(3, cliente.getDni());
            pstmt.setString(4, cliente.getEmail());
            pstmt.setString(5, cliente.getTelefono());

            // Ejecutamos el insert
            pstmt.executeUpdate();

            // Recuperamos el ID generado y se lo asignamos al objeto
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cliente.setId(rs.getLong(1));
                }
            }
            return cliente;

        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Fallo al guardar el cliente en la base de datos.", e);
        }
    }

    // METODO PARA BUSCAR UN CLIENTE POR SU DNI
    @Override
    public Optional<Cliente> buscarPorDni(String dni) {
        String sql = "SELECT * FROM clientes WHERE dni = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dni);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearCliente(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Fallo al buscar cliente por DNI.", e);
        }
        return Optional.empty();
    }

    // METODO PARA BUSCAR UN CLIENTE POR SU ID
    @Override
    public Optional<Cliente> buscarPorId(Long id) {
        String sql = "SELECT * FROM clientes WHERE id = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapearCliente(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Fallo al buscar cliente por ID.", e);
        }
        return Optional.empty();
    }

    // METODO PARA LISTAR TODOS LOS CLIENTES
    @Override
    public List<Cliente> listarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                clientes.add(mapearCliente(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Fallo al listar los clientes.", e);
        }
        return clientes;
    }

    //METODO ENCARGADO DE TRANSFORMAR UNA FILA DE LA TABLA EN OBJETO
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente(
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getString("dni"),
                rs.getString("email"),
                rs.getString("telefono")
        );
        c.setId(rs.getLong("id"));
        return c;
    }
}