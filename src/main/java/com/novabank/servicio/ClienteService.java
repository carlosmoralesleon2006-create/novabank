package com.novabank.servicio;

import com.novabank.modelo.Cliente;
import com.novabank.repositorio.Memoria;

import java.util.ArrayList;
import java.util.List;

public class ClienteService {

    private final Memoria memoria;

    public ClienteService(Memoria memoria) {
        this.memoria = memoria;
    }

    public Cliente crearCliente(String nombre, String apellidos, String dni, String email, String telefono) {
        if (nombre == null || nombre.isEmpty() || dni == null || dni.isEmpty() || email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Faltan datos obligatorios.");
        }

        for (Cliente c : memoria.clientes.values()) {
            if (c.getDni().equals(dni)) {
                throw new IllegalArgumentException("Ya existe un cliente con el DNI " + dni + ".");
            }
        }

        Cliente nuevoCliente = new Cliente(nombre, apellidos, dni, email, telefono);
        return memoria.guardarCliente(nuevoCliente);
    }

    public Cliente buscarPorDni(String dni) {
        for (Cliente c : memoria.clientes.values()) {
            if (c.getDni().equals(dni)) {
                return c;
            }
        }
        return null;
    }

    public Cliente buscarPorId(Long id) {
        return memoria.clientes.get(id);
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(memoria.clientes.values());
    }
}