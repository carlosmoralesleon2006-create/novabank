package com.novabank.repositorio;

import com.novabank.modelo.Movimiento;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public interface OperacionDAO {
    Movimiento guardarMovimiento(Movimiento movimiento, Connection conn);
    List<Movimiento> obtenerHistorialPorCuenta(String numeroCuenta);
    List<Movimiento> obtenerMovimientosPorRango(LocalDate inicio, LocalDate fin);
}