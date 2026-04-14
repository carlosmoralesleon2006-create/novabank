package com.novabank.repositorio;

import com.novabank.modelo.Movimiento;
import com.novabank.modelo.TipoMovimiento;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OperacionDAOimpl implements OperacionDAO {

    // GUARDA UN MOVIMIENTO EN LA BASE DE DATOS
    @Override
    public Movimiento guardarMovimiento(Movimiento movimiento) {
        String sql = "INSERT INTO movimientos (cuenta_id, tipo, cantidad) VALUES ((SELECT id FROM cuentas WHERE numero_cuenta = ?), ?, ?)";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Pasamos el número de cuenta (String). La subconsulta SQL lo convertirá en el ID.
            pstmt.setString(1, movimiento.getNumeroCuenta());

            // Guardamos el tipo de movimiento
            pstmt.setString(2, movimiento.getTipo().name());

            // Guardamos la cantidad
            pstmt.setBigDecimal(3, movimiento.getCantidad());

            // Ejecutamos el insert
            pstmt.executeUpdate();

            // Recuperamos el ID generado por la base de datos
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    movimiento.setId(rs.getLong(1));
                    movimiento.setFecha(rs.getObject("fecha", java.time.LocalDateTime.class));
                }
            }
            return movimiento;

        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Fallo al guardar el movimiento en la base de datos.", e);
        }
    }


    // OBTENER EL HISTORIAL DE MOVIMIENTOS DE UNA CUENTA
    @Override
    public List<Movimiento> obtenerHistorialPorCuenta(String numeroCuenta) {
        List<Movimiento> historial = new ArrayList<>();
        String sql = "SELECT m.id, c.numero_cuenta, m.tipo, m.cantidad, m.fecha " +
                "FROM movimientos m " +
                "JOIN cuentas c ON m.cuenta_id = c.id " +
                "WHERE c.numero_cuenta = ? " +
                "ORDER BY m.fecha DESC";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, numeroCuenta);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                historial.add(mapearMovimiento(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Fallo al obtener el historial de la cuenta.", e);
        }
        return historial;
    }


    // OBTENER HISTORIAL DE MOVIMIENTOS EN UN RANGO DE FECHAS
    @Override
    public List<Movimiento> obtenerMovimientosPorRango(LocalDate inicio, LocalDate fin) {
        List<Movimiento> movimientos = new ArrayList<>();
        String sql = "SELECT m.id, c.numero_cuenta, m.tipo, m.cantidad, m.fecha " +
                "FROM movimientos m " +
                "JOIN cuentas c ON m.cuenta_id = c.id " +
                "WHERE m.fecha BETWEEN ? AND ? " +
                "ORDER BY m.fecha DESC";

        try (Connection conn = ConexionDB.obtenerConexion();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setObject(1, inicio);
            pstmt.setObject(2, fin);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                movimientos.add(mapearMovimiento(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("ERROR: Fallo al obtener los movimientos por fecha.", e);
        }
        return movimientos;
    }

    // TRANSFORMA UNA FILA DE LA TABLA EN UN OBJETO
    private Movimiento mapearMovimiento(ResultSet rs) throws SQLException {
        Movimiento mov = new Movimiento(
                rs.getString("numero_cuenta"),
                TipoMovimiento.valueOf(rs.getString("tipo")),
                rs.getBigDecimal("cantidad")
        );
        mov.setId(rs.getLong("id"));
        mov.setFecha(rs.getObject("fecha", LocalDateTime.class));

        return mov;
    }
}