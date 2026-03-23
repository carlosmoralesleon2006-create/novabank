package com.novabank.servicio;

import com.novabank.modelo.Cliente;
import com.novabank.modelo.Cuenta;
import com.novabank.repositorio.Memoria;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CuentaService {

    private final Memoria storage;
    private final ClienteService clienteService;

    public CuentaService(Memoria storage, ClienteService clienteService) {
        this.storage = storage;
        this.clienteService = clienteService;
    }

    public Cuenta crearCuenta(Long clienteId) {
        Optional<Cliente> clienteOpt = clienteService.buscarPorId(clienteId);
        if (clienteOpt.isEmpty()) {
            throw new IllegalArgumentException("ERROR: No se encontró ningún cliente con ID " + clienteId);
        }

        String secuencia = String.format("%012d", storage.contadorCuentas);
        String numeroCuenta = "ES91210000" + secuencia;

        Cuenta nuevaCuenta = new Cuenta(numeroCuenta, clienteId);
        return storage.guardarCuenta(nuevaCuenta);
    }

    public List<Cuenta> listarCuentasDeCliente(Long clienteId) {
        return storage.cuentas.values().stream()
                .filter(cuenta -> cuenta.getClienteId().equals(clienteId))
                .collect(Collectors.toList());
    }

    public Optional<Cuenta> buscarPorNumero(String numeroCuenta) {
        return Optional.ofNullable(storage.cuentas.get(numeroCuenta));
    }
}