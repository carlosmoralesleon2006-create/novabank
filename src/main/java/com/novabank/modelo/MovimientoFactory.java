package com.novabank.modelo;

import java.math.BigDecimal;

public class MovimientoFactory {


    public static Movimiento crearDeposito(String numeroCuenta, BigDecimal cantidad) {
        return new Movimiento(numeroCuenta, TipoMovimiento.DEPOSITO, cantidad);
    }

    public static Movimiento crearRetiro(String numeroCuenta, BigDecimal cantidad) {
        return new Movimiento(numeroCuenta, TipoMovimiento.RETIRO, cantidad);
    }

    public static Movimiento crearTransferenciaSaliente(String numeroCuenta, BigDecimal cantidad) {
        return new Movimiento(numeroCuenta, TipoMovimiento.TRANSFERENCIA_SALIENTE, cantidad);
    }

    public static Movimiento crearTransferenciaEntrante(String numeroCuenta, BigDecimal cantidad) {
        return new Movimiento(numeroCuenta, TipoMovimiento.TRANSFERENCIA_ENTRANTE, cantidad);
    }
}