package com.novabank.servicio;

import com.novabank.modelo.Cuenta;
import com.novabank.modelo.Movimiento;
import com.novabank.modelo.TipoMovimiento;
import com.novabank.repositorio.Memoria;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class OperacionService {

    private final Memoria memoria;
    private final CuentaService cuentaService;

    public OperacionService(Memoria memoria, CuentaService cuentaService) {
        this.memoria = memoria;
        this.cuentaService = cuentaService;
    }

    public void depositar(String numeroCuenta, BigDecimal cantidad) {
        validarCantidadPositiva(cantidad);
        Cuenta cuenta = obtenerCuentaExistente(numeroCuenta);

        cuenta.setSaldo(cuenta.getSaldo().add(cantidad));
        Movimiento mov = new Movimiento(numeroCuenta, TipoMovimiento.DEPOSITO, cantidad);
        memoria.guardarMovimiento(mov);
    }

    public void retirar(String numeroCuenta, BigDecimal cantidad) {
        validarCantidadPositiva(cantidad);
        Cuenta cuenta = obtenerCuentaExistente(numeroCuenta);

        if (cuenta.getSaldo().compareTo(cantidad) < 0) {
            throw new IllegalArgumentException(String.format(
                    "ERROR: Saldo insuficiente.\nSaldo disponible: %.2f €\nImporte solicitado: %.2f €",
                    cuenta.getSaldo(), cantidad));
        }

        cuenta.setSaldo(cuenta.getSaldo().subtract(cantidad));
        Movimiento mov = new Movimiento(numeroCuenta, TipoMovimiento.RETIRO, cantidad);
        memoria.guardarMovimiento(mov);
    }

    public void transferir(String cuentaOrigen, String cuentaDestino, BigDecimal cantidad) {
        if (cuentaOrigen.equals(cuentaDestino)) {
            throw new IllegalArgumentException("La cuenta origen y la cuenta destino no pueden ser la misma.");
        }
        validarCantidadPositiva(cantidad);

        Cuenta origen = obtenerCuentaExistente(cuentaOrigen);
        Cuenta destino = obtenerCuentaExistente(cuentaDestino);

        if (origen.getSaldo().compareTo(cantidad) < 0) {
            throw new IllegalArgumentException("ERROR: Saldo insuficiente en la cuenta origen.");
        }

        origen.setSaldo(origen.getSaldo().subtract(cantidad));

        try {
            destino.setSaldo(destino.getSaldo().add(cantidad));

            memoria.guardarMovimiento(new Movimiento(cuentaOrigen, TipoMovimiento.TRANSFERENCIA_SALIENTE, cantidad));
            
            memoria.guardarMovimiento(new Movimiento(cuentaDestino, TipoMovimiento.TRANSFERENCIA_ENTRANTE, cantidad));
            
        } catch (Exception e) {
            origen.setSaldo(origen.getSaldo().add(cantidad));
            throw new RuntimeException("Error inesperado en la transferencia. Operación revertida.", e);
        }
    }

    // --- CONSULTA ---

    public List<Movimiento> obtenerHistorial(String numeroCuenta) {
        obtenerCuentaExistente(numeroCuenta);
        
        return memoria.movimientos.values().stream()
                .filter(m -> m.getNumeroCuenta().equals(numeroCuenta))
                .sorted(Comparator.comparing(Movimiento::getFecha).reversed())
                .collect(Collectors.toList());
    }

    public List<Movimiento> obtenerHistorialPorRango(String numeroCuenta, LocalDate inicio, LocalDate fin) {
        return obtenerHistorial(numeroCuenta).stream()
                .filter(m -> {
                    LocalDate fechaMov = m.getFecha().toLocalDate();
                    return !fechaMov.isBefore(inicio) && !fechaMov.isAfter(fin);
                })
                .collect(Collectors.toList());
    }

    // --- AUXILIARES ---

    private void validarCantidadPositiva(BigDecimal cantidad) {
        if (cantidad == null || cantidad.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero.");
        }
    }

    private Cuenta obtenerCuentaExistente(String numeroCuenta) {
        return cuentaService.buscarPorNumero(numeroCuenta)
                .orElseThrow(() -> new IllegalArgumentException("La cuenta " + numeroCuenta + " no existe."));
    }
}