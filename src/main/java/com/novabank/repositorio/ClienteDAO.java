package com.novabank.repositorio;

import com.novabank.modelo.Cliente;
import java.util.List;
import java.util.Optional;

public interface ClienteDAO {
    Cliente guardar(Cliente cliente);
    Optional buscarPorDni(String dni);
    Optional buscarPorId(Long id);
    List<Cliente> listarTodos();
}