package com.novabank.servicio;

import com.novabank.modelo.Cliente;
import com.novabank.repositorio.Memoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ClienteService {

    private final Memoria memoria;

    public ClienteService(Memoria memoria) {
        this.memoria = memoria;
    }

    public Cliente crearCliente(String nombre, String apellidos, String dni, String email, String telefono) {
        if (nombre == null || nombre.isBlank() ||
            apellidos == null || apellidos.isBlank() ||
            dni == null || dni.isBlank() ||
            email == null || email.isBlank() ||
            telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("Todos los campos son obligatorios.");
        }

        int atIndex = email.indexOf('@');
        if (atIndex == -1 || email.indexOf('.', atIndex) == -1) {
            throw new IllegalArgumentException("El formato del email no es válido.");
        }

        for (Cliente c : memoria.clientes.values()) {
            if (c.getDni().equalsIgnoreCase(dni)) {
                throw new IllegalArgumentException("Ya existe un cliente con el DNI " + dni + ".");
            }
            if (c.getEmail().equalsIgnoreCase(email)) {
                throw new IllegalArgumentException("Ya existe un cliente con el email " + email + ".");
            }
            if (c.getTelefono().equals(telefono)) {
                throw new IllegalArgumentException("Ya existe un cliente con el teléfono " + telefono + ".");
            }
        }

        Cliente nuevoCliente = new Cliente(nombre, apellidos, dni, email, telefono);
        return memoria.guardarCliente(nuevoCliente);
    }

    public Optional<Cliente> buscarPorDni(String dni) {
        return memoria.clientes.values().stream()
                .filter(c -> c.getDni().equalsIgnoreCase(dni))
                .findFirst();
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return Optional.ofNullable(memoria.clientes.get(id));
    }

    public List<Cliente> listarClientes() {
        return new ArrayList<>(memoria.clientes.values());
    }
}