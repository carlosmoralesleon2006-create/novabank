package com.novabank.repositorio;

import com.novabank.modelo.Cuenta;
import java.util.List;
import java.util.Optional;

public interface CuentaDAO {
    Cuenta guardar(Cuenta cuenta);
    Optional<Cuenta> buscarPorNumeroDeCuenta(String numeroCuenta);
    List<Cuenta> listarPorCliente(Long clienteId);
    List<Cuenta> listarTodas();
    void actualizar(Cuenta cuenta);
    long obtenerSiguienteNumeroSecuencial();
}