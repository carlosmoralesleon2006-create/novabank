package com.novabank.servicio;

import com.novabank.modelo.Cliente;
import com.novabank.repositorio.ClienteDAO;

import java.util.List;
import java.util.Optional;

public class ClienteService {

    private final ClienteDAO clienteDAO;

    public ClienteService(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    public Cliente crearCliente(String nombre, String apellidos, String dni, String email, String telefono) {
        validarFormatoDatos(nombre, apellidos, dni, email, telefono);

        if (clienteDAO.buscarPorDni(dni).isPresent()) {
            throw new IllegalArgumentException("Ya existe un cliente con el DNI " + dni + ".");
        }

        validarUnico(email, telefono);

        Cliente nuevoCliente = new Cliente.ClienteBuilder()
                .conNombre(nombre)
                .conApellidos(apellidos)
                .conDni(dni)
                .conEmail(email)
                .conTelefono(telefono)
                .build();
        return clienteDAO.guardar(nuevoCliente);
    }

    // MÉTODO ENCARGADO DE VALIDAR LOS DATOS DEL CLIENTE
    private void validarFormatoDatos(String nombre, String apellidos, String dni, String email, String telefono) {
        if (nombre == null || nombre.isBlank() || apellidos == null || apellidos.isBlank() ||
                dni == null || dni.isBlank() || email == null || email.isBlank() || telefono == null || telefono.isBlank()) {
            throw new IllegalArgumentException("Faltan datos obligatorios.");
        }
        if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]{2,50}$")) throw new IllegalArgumentException("Nombre inválido.");
        if (!apellidos.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑüÜ\\s]{2,50}$")) throw new IllegalArgumentException("Apellidos inválidos.");
        if (!dni.matches("^[0-9]{8}[A-Za-z]$")) throw new IllegalArgumentException("DNI inválido.");
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) throw new IllegalArgumentException("Email inválido.");
        if (!telefono.matches("^[6789][0-9]{8}$")) throw new IllegalArgumentException("Teléfono inválido.");
    }

    private void validarUnico(String email, String telefono) {
        List<Cliente> todos = clienteDAO.listarTodos();

        boolean emailRepetido = todos.stream().anyMatch(c -> c.getEmail().equalsIgnoreCase(email));
        if (emailRepetido) throw new IllegalArgumentException("Ya existe un cliente con el email " + email + ".");

        boolean telRepetido = todos.stream().anyMatch(c -> c.getTelefono().equals(telefono));
        if (telRepetido) throw new IllegalArgumentException("Ya existe un cliente con el teléfono " + telefono + ".");
    }

    public Optional<Cliente> buscarPorDni(String dni) {
        return clienteDAO.buscarPorDni(dni);
    }

    public Optional<Cliente> buscarPorId(Long id) {
        return clienteDAO.buscarPorId(id);
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listarTodos();
    }
}