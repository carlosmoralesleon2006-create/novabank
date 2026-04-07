package com.novabank.repositorio;

import com.novabank.modelo.Cliente;
import com.novabank.modelo.Cuenta;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CuentaDAO {

    public CuentaDAO(){}

    // METODO PARA GUARDAR UNA CUENTA NUEVA EN LA BASE DE DATOS
    public Cuenta guardar(Cuenta cuenta) {
        String sql = "INSERT INTO cuentas (numero_cuenta, cliente_id, saldo, fecha_creacion) VALUES (?, ?, ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
             // Le decimos a Java que queremos recuperar el ID que PostgreSQL genera automáticamente
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Rellenamos los interrogantes con los datos del cliente proporcionado
            pstmt.setString(1, cuenta.getNumeroCuenta());
            pstmt.setLong(2, cuenta.getClienteId());
            pstmt.setBigDecimal(3, cuenta.getSaldo());
            pstmt.setObject(4, cuenta.getFechaCreacion());

            // Ejecutamos el insert
            pstmt.executeUpdate();

            // Recuperamos el ID generado y se lo asignamos al objeto
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    cuenta.setId(rs.getLong(1));
                }
            }
            return cuenta;

        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Fallo al guardar el cliente en la base de datos.", e);
        }
    }

    // METODO PARA BUSCAR UNA POR SU NÚMERO DE CUENTA
    public Cuenta buscarPorNumeroDeCuenta(String numeroCuenta) {
        String sql = "SELECT * FROM cuentas WHERE numero_cuenta = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, numeroCuenta);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearCuenta(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Fallo al buscar la cuenta por numero de cuenta.", e);
        }
        return null; // Devuelve null si no la encuentra
    }

    // METODO PARA BUSCAR UNA CUENTA POR SU ID
    public List<Cuenta> listarPorCliente(Long clienteId){
        List<Cuenta> cuentasDelCliente = new ArrayList<>();
        String sql = "SELECT * FROM cuentas WHERE cliente_id = " + clienteId;

        try (Connection conn = ConexionDB.obtenerConexion();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()){

            while(rs.next()){
                cuentasDelCliente.add(mapearCuenta(rs));
            }

        } catch (SQLException e){
            throw new RuntimeException("ERROR: Fallo al listar las cuentas del cliente", e);
        }
        return cuentasDelCliente;
    }

    // METODO PARA LISTAR TODAS LAS CUENTAS
    public List<Cuenta> listarTodas() {
        List<Cuenta> cuentas = new ArrayList<>();
        String sql = "SELECT * FROM cuentas";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                cuentas.add(mapearCuenta(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Fallo al listar las cuentas.", e);
        }
        return cuentas;
    }

    // METODO PARA ACTUALIZAR LOS DATOS DE UNA CUENTA
    public void actualizar(Cuenta cuenta) {
        // Hacemos un UPDATE usando el ID de la cuenta
        String sql = "UPDATE cuentas SET saldo = ? WHERE id = ?";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Rellenamos los datos
            pstmt.setBigDecimal(1, cuenta.getSaldo());
            pstmt.setLong(2, cuenta.getId());

            // Ejecutamos la actualización
            int filasAfectadas = pstmt.executeUpdate();

            if (filasAfectadas == 0) {
                throw new RuntimeException("ERROR: No se encontró la cuenta para actualizar en la BD.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Fallo al actualizar la cuenta en la base de datos.", e);
        }
    }

    //METODO ENCARGADO DE TRANSFORMAR UNA FILA DE LA TABLA EN OBJETO
    private Cuenta mapearCuenta(ResultSet rs) throws SQLException {
        Cuenta c = new Cuenta(
                rs.getString("numero_cuenta"),
                rs.getLong("cliente_id")
        );
        c.setId(rs.getLong("id"));

        c.setSaldo(rs.getBigDecimal("saldo"));

        c.setFechaCreacion(rs.getObject("fecha_creacion", java.time.LocalDateTime.class));

        return c;
    }


    // METODO ENCARGADO DE OBTENER EL SIGUIENTE NÚMERO CON EL FORMATO NECESARIO
    public long obtenerSiguienteNumeroSecuencial() {
        // Buscamos el ID más alto, si la tabla está vacía, devuelve 0.
        String sql = "SELECT COALESCE(MAX(id), 0) + 1 FROM cuentas";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                return rs.getLong(1); // Devuelve ese (máximo + 1)
            }
        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Fallo al calcular el secuencial de cuenta.", e);
        }
        return 1L; // Por defecto
    }
}