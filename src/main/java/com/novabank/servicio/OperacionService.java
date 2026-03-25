package com.novabank.servicio;

import com.novabank.modelo.Cuenta;
import com.novabank.modelo.Movimiento;
import com.novabank.modelo.TipoMovimiento;
import com.novabank.repositorio.Memoria;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EmptyStackException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

public class OperacionService {

    private final Memoria memoria;
    private final CuentaService cuentaService;

    public OperacionService(Memoria memoria, CuentaService cuentaService) {
        this.memoria = memoria;
        this.cuentaService = cuentaService;
    }

    //METODO ENCARGADO DE DEPOSITAR DINERO EN EL SALDO DE UNA CUENTA
    public void depositar(String numeroCuenta, BigDecimal cantidad) {
        Cuenta cuenta = cuentaService.buscarPorNumero(numeroCuenta);
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no existe.");
        }

        //Añadimos la cantidad al saldo
        cuenta.setSaldo(cuenta.getSaldo().add(cantidad));

        //Creamos y guardamos el movimiento
        Movimiento mov = new Movimiento(numeroCuenta, TipoMovimiento.DEPOSITO, cantidad);
        memoria.guardarMovimiento(mov);
    }

    //METODO ENCARGADO DE RETIRAR DINERO DEL SALDO DE UNA CUENTA
    public void retirar(String numeroCuenta, BigDecimal cantidad) {
        Cuenta cuenta = cuentaService.buscarPorNumero(numeroCuenta);
        if (cuenta == null) {
            throw new IllegalArgumentException("La cuenta no existe.");
        }

        // Compararamos que el saldo sea mayor o igual a la cantidad que se quiere retirar
        if (cuenta.getSaldo().compareTo(cantidad) < 0) {
            throw new IllegalArgumentException("ERROR: Saldo insuficiente.");
        }

        cuenta.setSaldo(cuenta.getSaldo().subtract(cantidad));
        Movimiento mov = new Movimiento(numeroCuenta, TipoMovimiento.RETIRO, cantidad);
        memoria.guardarMovimiento(mov);
    }

    //METODO ENCARGADO DE TRANSFERIR DINERO DE UNA CUENTA DE ORIGEN A UNA CUENTA DE DESTINO
    public void transferir(String cuentaOrigen, String cuentaDestino, BigDecimal cantidad) {
        Cuenta origen = cuentaService.buscarPorNumero(cuentaOrigen);
        Cuenta destino = cuentaService.buscarPorNumero(cuentaDestino);

        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Una de las cuentas no existe.");
        }
        if (origen.getSaldo().compareTo(cantidad) < 0) {
            throw new IllegalArgumentException("ERROR: Saldo insuficiente.");
        }
        if(origen.getNumeroCuenta().equals(destino.getNumeroCuenta())){
            throw new IllegalArgumentException("ERROR: No es posible transeferir dinero hacia la misma cuenta.");
        }

        //Retiramos el saldo de la cuenta origen
        origen.setSaldo(origen.getSaldo().subtract(cantidad));

        try {
            //Depositamos el dinero en la cuenta destino
            destino.setSaldo(destino.getSaldo().add(cantidad));

            //Guardamos los movimientos
            memoria.guardarMovimiento(new Movimiento(cuentaOrigen, TipoMovimiento.TRANSFERENCIA_SALIENTE, cantidad));
            memoria.guardarMovimiento(new Movimiento(cuentaDestino, TipoMovimiento.TRANSFERENCIA_ENTRANTE, cantidad));

        } catch (Exception e) {
            //Si algo falla devolvemos el saldo a su estado original
            origen.setSaldo(origen.getSaldo().add(cantidad));
            throw new RuntimeException("Error en la transferencia");
        }
    }

    //METODO ENCARGADO DE OBTENER EL HISTORIAL DE MOVIMIENTOS DE UNA CUENTA
    public List<Movimiento> obtenerHistorial(String numeroCuenta) {
        List<Movimiento> historial = new ArrayList<>();
        //Guardamos solo los movimientos de esta cuenta
        for (Movimiento m : memoria.movimientos.values()) {
            if (m.getNumeroCuenta().equals(numeroCuenta)) {
                historial.add(m);
            }
        }

        //Ordenamos la lista del más antiguo al más reciente
        historial.sort((m1, m2) -> m2.getFecha().compareTo(m1.getFecha()));

        return historial;
    }

    //METODO ENCARGADO DE OBTENER UNA LISTA DE MOVIMIENTOS EN UN RANGO DE FECHAS
    public List<Movimiento> obtenerMovimientosRangoFecha(String inicio, String fin) {
        LocalDate fInicioFormat;
        LocalDate fFinFormat;
        try {
            fInicioFormat = LocalDate.parse(inicio, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            fFinFormat = LocalDate.parse(fin, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception d) {
            throw new IllegalArgumentException("El formato de las fechas introducidas es incorrecto");
        }

        if (fInicioFormat.isAfter(fFinFormat)) {
            throw new IllegalArgumentException("Rango de fechas inválido");
        }
        if (memoria.movimientos.isEmpty()) {
            throw new IllegalArgumentException("No hay ningún movimiento registrado.");
        }
        List<Movimiento> movFechas = memoria.movimientos.values().stream().filter(movimiento -> movimiento.getFecha() != null).filter(movimiento -> !movimiento.getFecha().toLocalDate().isBefore(fInicioFormat) ||
                movimiento.getFecha().toLocalDate().isEqual(fInicioFormat) &&
                        !movimiento.getFecha().toLocalDate().isAfter(fFinFormat) || movimiento.getFecha().toLocalDate().isEqual(fFinFormat)).toList();
        if (movFechas.isEmpty()) {
            System.out.println("No hay ningún movimiento registrado en el rango de fechas solicitado.");
        }
        return movFechas;
    }
}