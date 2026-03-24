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
    
    public long contadorClientes = 1000L; //Contador para asignar los identificadores únicos de los clientes
    public long contadorCuentas = 1L; //Contador para asignar los identificadores únicos de las cuentas
    public long contadorMovimientos = 1L; //Contador para asignar los identificadores únicos de los movimientos



    //METODO ENCARGADO DE GUARDAR UN CLIENTE EN LA LISTA CORRESPONDIENTE ASIGNANDOLE EL IDENTIFICADOR ÚNICO
    public Cliente guardarCliente(Cliente cliente) {
        cliente.setId(contadorClientes++);
        clientes.put(cliente.getId(), cliente);
        return cliente;
    }

    //METODO ENCARGADO DE GUARDAR UNA CUENTA EN LA LISTA CORRESPONDIENTE ASIGNANDOLE EL IDENTIFICADOR ÚNICO
    public Cuenta guardarCuenta(Cuenta cuenta) {
        cuenta.setId(contadorCuentas++);
        cuentas.put(cuenta.getNumeroCuenta(), cuenta);
        return cuenta;
    }

    //METODO ENCARGADO DE GUARDAR UN MOVIMIENTO EN LA LISTA CORRESPONDIENTE ASIGNANDOLE EL IDENTIFICADOR ÚNICO
    public Movimiento guardarMovimiento(Movimiento movimiento) {
        movimiento.setId(contadorMovimientos++);
        movimientos.put(movimiento.getId(), movimiento);
        return movimiento;
    }
}