package com.novabank.repositorio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:postgresql://localhost:5432/novabank_db";
    private static final String USUARIO = "postgres";
    private static final String PASSWORD = "root";

    public static Connection obtenerConexion() {
        try {
            return DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (SQLException e) {
            throw new RuntimeException("ERROR: No se pudo conectar a la base de datos. Verifica que PostgreSQL esté encendido y las credenciales sean correctas.", e);
        }
    }
}