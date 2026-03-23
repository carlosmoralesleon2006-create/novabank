package com.novabank.repositorio;

import com.novabank.modelo.Cliente;
import com.novabank.modelo.Cuenta;
import com.novabank.modelo.Movimiento;

import java.util.HashMap;
import java.util.Map;

public class Memoria {
    
    public final Map<Long, Cliente> clientes = new HashMap<>();
    public final Map<String, Cuenta> cuentas = new HashMap<>();
    public final Map<Long, Movimiento> movimientos = new HashMap<>();
    
    public long contadorClientes = 1000L;
    public long contadorCuentas = 1L;
    public long contadorMovimientos = 1L;
    
    public Cliente guardarCliente(Cliente cliente) {
        cliente.setId(contadorClientes++);
        clientes.put(cliente.getId(), cliente);
        return cliente;
    }

    public Cuenta guardarCuenta(Cuenta cuenta) {
        cuenta.setId(contadorCuentas++);
        cuentas.put(cuenta.getNumeroCuenta(), cuenta);
        return cuenta;
    }

    public Movimiento guardarMovimiento(Movimiento movimiento) {
        movimiento.setId(contadorMovimientos++);
        movimientos.put(movimiento.getId(), movimiento);
        return movimiento;
    }
}