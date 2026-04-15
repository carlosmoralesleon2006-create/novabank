package com.novabank.servicio;

import com.novabank.modelo.Cuenta;
import com.novabank.modelo.Movimiento;
import com.novabank.modelo.MovimientoFactory;
import com.novabank.repositorio.ConexionDB;
import com.novabank.repositorio.OperacionDAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class OperacionService {

    private final OperacionDAO operacionDAO;
    private final CuentaService cuentaService;

    public OperacionService(OperacionDAO operacionDAO, CuentaService cuentaService) {
        this.operacionDAO = operacionDAO;
        this.cuentaService = cuentaService;
    }

    //MÉTODO PARA DEPOSITAR DINERO EN UNA CUENTA
    public void depositar(String numeroCuenta, BigDecimal cantidad) {
        Cuenta cuenta = cuentaService.buscarPorNumero(numeroCuenta)
                .orElseThrow(() -> new IllegalArgumentException("La cuenta no existe."));

        cuenta.setSaldo(cuenta.getSaldo().add(cantidad));

        try (Connection conn = ConexionDB.obtenerConexion()) {
            conn.setAutoCommit(false);

            try {
                cuentaService.actualizar(cuenta, conn);
                Movimiento mov = MovimientoFactory.crearDeposito(numeroCuenta, cantidad);
                operacionDAO.guardarMovimiento(mov, conn);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                cuenta.setSaldo(cuenta.getSaldo().subtract(cantidad));
                throw new RuntimeException("Error al depositar: " + e.getMessage());
            }
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Error grave de conexión durante el depósito.", e);
        }
    }


    //MÉTODO PARA RETIRAR DINERO DE UNA CUENTA
    public void retirar(String numeroCuenta, BigDecimal cantidad) {
        Cuenta cuenta = cuentaService.buscarPorNumero(numeroCuenta)
                .orElseThrow(() -> new IllegalArgumentException("La cuenta no existe."));

        if (cuenta.getSaldo().compareTo(cantidad) < 0) {
            throw new IllegalArgumentException("ERROR: Saldo insuficiente.");
        }

        cuenta.setSaldo(cuenta.getSaldo().subtract(cantidad));

        try (Connection conn = ConexionDB.obtenerConexion()) {
            conn.setAutoCommit(false);

            try {
                cuentaService.actualizar(cuenta, conn);
                Movimiento mov = MovimientoFactory.crearRetiro(numeroCuenta, cantidad);
                operacionDAO.guardarMovimiento(mov, conn);

                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                cuenta.setSaldo(cuenta.getSaldo().add(cantidad));
                throw new RuntimeException("Error al retirar: " + e.getMessage());
            }
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Error grave de conexión durante el retiro.", e);
        }
    }


    //MÉTODO PARA TRANSFERIR DINERO DE UNA CUENTA A OTRA
    public void transferir(String cuentaOrigen, String cuentaDestino, BigDecimal cantidad) {
        Cuenta origen = cuentaService.buscarPorNumero(cuentaOrigen)
                .orElseThrow(() -> new IllegalArgumentException("La cuenta de origen no existe."));

        Cuenta destino = cuentaService.buscarPorNumero(cuentaDestino)
                .orElseThrow(() -> new IllegalArgumentException("La cuenta de destino no existe."));

        if (origen.getSaldo().compareTo(cantidad) < 0) {
            throw new IllegalArgumentException("ERROR: Saldo insuficiente.");
        }
        if (origen.getNumeroCuenta().equals(destino.getNumeroCuenta())) {
            throw new IllegalArgumentException("ERROR: No es posible transferir dinero hacia la misma cuenta.");
        }

        origen.setSaldo(origen.getSaldo().subtract(cantidad));
        destino.setSaldo(destino.getSaldo().add(cantidad));

        try (Connection conn = ConexionDB.obtenerConexion()) {

            conn.setAutoCommit(false);

            try {
                cuentaService.actualizar(origen, conn);
                cuentaService.actualizar(destino, conn);

                operacionDAO.guardarMovimiento(MovimientoFactory.crearTransferenciaSaliente(cuentaOrigen, cantidad), conn);
                operacionDAO.guardarMovimiento(MovimientoFactory.crearTransferenciaEntrante(cuentaDestino, cantidad), conn);

                conn.commit();

            } catch (Exception e) {
                conn.rollback();

                origen.setSaldo(origen.getSaldo().add(cantidad));
                destino.setSaldo(destino.getSaldo().subtract(cantidad));
                throw new RuntimeException("Error en la transferencia. Se ha realizado un rollback: " + e.getMessage());
            }
        } catch (java.sql.SQLException e) {
            throw new RuntimeException("Error grave de conexión durante la transferencia.", e);
        }
    }

    // OBTENER HISTORIAL DE MOVIMIENTOS DE UNA CUENTA
    public List<Movimiento> obtenerHistorial(String numeroCuenta) {
        return operacionDAO.obtenerHistorialPorCuenta(numeroCuenta);
    }

    // OBTIENE EL HISTORIAL DE MOVIMIENTOS EN UN RANGO DE FECHAS
    public List<Movimiento> obtenerMovimientosRangoFecha(String inicio, String fin) {
        LocalDate fInicioFormat;
        LocalDate fFinFormat;
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            fInicioFormat = LocalDate.parse(inicio, formatter);
            fFinFormat = LocalDate.parse(fin, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("El formato de las fechas introducidas es incorrecto. Use dd/MM/yyyy.");
        }

        if (fInicioFormat.isAfter(fFinFormat)) {
            throw new IllegalArgumentException("Rango de fechas inválido. La fecha de inicio debe ser anterior a la fecha de fin.");
        }

        List<Movimiento> movFechas = operacionDAO.obtenerMovimientosPorRango(fInicioFormat, fFinFormat);

        if (movFechas.isEmpty()) {
            throw new IllegalArgumentException("No hay ningún movimiento registrado en el rango de fechas solicitado.");
        }

        return movFechas;
    }
}