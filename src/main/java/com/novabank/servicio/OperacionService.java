package com.novabank.servicio;

import com.novabank.modelo.Cuenta;
import com.novabank.modelo.Movimiento;
import com.novabank.modelo.TipoMovimiento;
import com.novabank.servicio.CuentaService;
import com.novabank.repositorio.OperacionDAO;

import java.math.BigDecimal;
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

    public void depositar(String numeroCuenta, BigDecimal cantidad) {
        Cuenta cuenta = cuentaService.buscarPorNumero(numeroCuenta);
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no existe.");
        }

        cuenta.setSaldo(cuenta.getSaldo().add(cantidad));
        cuentaService.actualizar(cuenta);

        Movimiento mov = new Movimiento(numeroCuenta, TipoMovimiento.DEPOSITO, cantidad);
        operacionDAO.guardarMovimiento(mov);
    }

    public void retirar(String numeroCuenta, BigDecimal cantidad) {
        Cuenta cuenta = cuentaService.buscarPorNumero(numeroCuenta);
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no existe.");
        }
        if (cuenta.getSaldo().compareTo(cantidad) < 0) {
            throw new IllegalArgumentException("ERROR: Saldo insuficiente.");
        }

        cuenta.setSaldo(cuenta.getSaldo().subtract(cantidad));
        cuentaService.actualizar(cuenta);

        Movimiento mov = new Movimiento(numeroCuenta, TipoMovimiento.RETIRO, cantidad);
        operacionDAO.guardarMovimiento(mov);
    }

    public void transferir(String cuentaOrigen, String cuentaDestino, BigDecimal cantidad) {
        Cuenta origen = cuentaService.buscarPorNumero(cuentaOrigen);
        Cuenta destino = cuentaService.buscarPorNumero(cuentaDestino);

        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Una de las cuentas no existe.");
        }
        if (origen.getSaldo().compareTo(cantidad) < 0) {
            throw new IllegalArgumentException("ERROR: Saldo insuficiente.");
        }
        if (origen.getNumeroCuenta().equals(destino.getNumeroCuenta())) {
            throw new IllegalArgumentException("ERROR: No es posible transferir dinero hacia la misma cuenta.");
        }

        origen.setSaldo(origen.getSaldo().subtract(cantidad));
        destino.setSaldo(destino.getSaldo().add(cantidad));

        try {
            cuentaService.actualizar(origen);
            cuentaService.actualizar(destino);

            operacionDAO.guardarMovimiento(new Movimiento(cuentaOrigen, TipoMovimiento.TRANSFERENCIA_SALIENTE, cantidad));
            operacionDAO.guardarMovimiento(new Movimiento(cuentaDestino, TipoMovimiento.TRANSFERENCIA_ENTRANTE, cantidad));

        } catch (Exception e) {
            origen.setSaldo(origen.getSaldo().add(cantidad));
            destino.setSaldo(destino.getSaldo().subtract(cantidad));
            throw new RuntimeException("Error en la transferencia: " + e.getMessage());
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