package com.novabank.servicio;

import com.novabank.modelo.Cliente;
import com.novabank.repositorio.ClienteDAO;

import java.util.ArrayList;
import java.util.List;

public class ClienteService {


    ClienteDAO clienteDAO = new ClienteDAO();
    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }


    //METODO ENCARGADO DE CREAR UN CLIENTE VALIDANDO SUS ATRIBUTOS Y ALMACENARLO EN MEMORIA
    public Cliente crearCliente(String nombre, String apellidos, String dni, String email, String telefono) {

        // Validamos que ningún dato venga vacío o nulo (todos son obligatorios)
        if (nombre == null || nombre.isEmpty() ||
                apellidos == null || apellidos.isEmpty() ||
                dni == null || dni.isEmpty() ||
                email == null || email.isEmpty() ||
                telefono == null || telefono.isEmpty()) {
            throw new IllegalArgumentException("Faltan datos obligatorios.");
        }

        // Validaciones de formato
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]{2,50}$")) {
            throw new IllegalArgumentException("El nombre solo puede contener letras y debe tener entre 2 y 50 caracteres.");
        }
        if (!apellidos.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]{2,50}$")) {
            throw new IllegalArgumentException("Los apellidos solo pueden contener letras y deben tener entre 2 y 50 caracteres.");
        }
        if (!dni.matches("^[0-9]{8}[A-Za-z]$")) {
            throw new IllegalArgumentException("El DNI se ha introducido en un formato incorrecto (8 números y 1 letra).");
        }
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new IllegalArgumentException("El email se ha introducido en un formato incorrecto.");
        }
        if (!telefono.matches("^[6789][0-9]{8}$")) {
            throw new IllegalArgumentException("El teléfono debe tener 9 dígitos y empezar por 6, 7, 8 o 9.");
        }


        // Validamos que DNI, Email y Teléfono sean únicos
        for (Cliente c : clienteDAO.listarTodos()) {
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
        return clienteDAO.guardar(nuevoCliente);
    }

    //METODO ENCARGADO DE BUSCAR UN CLIENTE POR SU DNI
    public Cliente buscarPorDni(String dni) {
        for (Cliente c : clienteDAO.listarTodos()) {
            if (c.getDni().equals(dni)) {
                return c;
            }
        }
        return null;
    }

    //METODO ENCARGADO DE BUSCAR UN CLIENTE POR SU IDENTIFICADOR ÚNICO
    public Cliente buscarPorId(Long id) {
        return clienteDAO.buscarPorId(id);
    }

    //METODO ENCARGADO DE LISTAR TODOS LOS CLIENTES ALMACENADOS
    public List<Cliente> listarClientes() {
        return new ArrayList<>(clienteDAO.listarTodos());
    }
}