package com.novabank.servicio;

import com.novabank.modelo.Cliente;
import com.novabank.modelo.Cuenta;
import com.novabank.repositorio.Memoria;
import java.util.ArrayList;
import java.util.List;

public class CuentaService {

    private final Memoria memoria;
    private final ClienteService clienteService;

    public CuentaService(Memoria memoria, ClienteService clienteService) {
        this.memoria = memoria;
        this.clienteService = clienteService;
    }

    //METODO QUE USA LA ID DEL CLIENTE PARA CREAR UNA CUENTA NUEVA
    public Cuenta crearCuenta(Long clienteId) {

        //Usamos el método para buscar un cliente por su id
        Cliente cliente = clienteService.buscarPorId(clienteId);

        //Si no se encuentra el cliente lanzamos una excepción
        if (cliente == null) {
            throw new IllegalArgumentException("ERROR: No se encontró ningún cliente con ID " + clienteId);
        }

        //String con el prefijo por defecto de la cuenta
        String prefijo = "ES91210000";
        long numero = memoria.contadorCuentas;

        //Preparamos el número y lo juntamos con el prefijo (solo le añadimos ceros al contador)
        String numeroFormateado = String.format("%012d", numero);
        String numeroCuenta = prefijo + numeroFormateado;

        //Creamos la cuenta nueva
        Cuenta nuevaCuenta = new Cuenta(numeroCuenta, clienteId);
        return memoria.guardarCuenta(nuevaCuenta);
    }

    //METODO ENCARGADO DE DEVOLVER UNA LISTA DE LAS CUENTAS DE UN CLIENTE POR SU ID
    public List<Cuenta> listarCuentasDeCliente(Long clienteId) {
        List<Cuenta> lista = new ArrayList<>();
        for (Cuenta cuenta : memoria.cuentas.values()) {
            if (cuenta.getClienteId().equals(clienteId)) {
                lista.add(cuenta);
            }
        }
        return lista;
    }

    public Cuenta buscarPorNumero(String numeroCuenta) {
        return memoria.cuentas.get(numeroCuenta);
    }
}