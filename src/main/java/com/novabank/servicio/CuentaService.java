package com.novabank.servicio;

import com.novabank.modelo.Cliente;
import com.novabank.modelo.Cuenta;
import com.novabank.repositorio.CuentaDAO;
import java.util.ArrayList;
import java.util.List;

public class CuentaService {

    private final CuentaDAO cuentaDAO;
    private final ClienteService clienteService;

    public CuentaService(CuentaDAO cuentaDAO, ClienteService clienteService) {
        this.cuentaDAO = cuentaDAO;
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
        long numero = cuentaDAO.obtenerSiguienteNumeroSecuencial();

        //Preparamos el número y lo juntamos con el prefijo (solo le añadimos ceros al contador)
        String numeroFormateado = String.format("%012d", numero);
        String numeroCuenta = prefijo + numeroFormateado;

        //Creamos la cuenta nueva
        Cuenta nuevaCuenta = new Cuenta(numeroCuenta, clienteId);
        return cuentaDAO.guardar(nuevaCuenta);
    }

    //METODO ENCARGADO DE DEVOLVER UNA LISTA DE LAS CUENTAS DE UN CLIENTE POR SU ID
    public List<Cuenta> listarCuentasDeCliente(Long clienteId) {
        return cuentaDAO.listarPorCliente(clienteId);
    }

    //METODO ENCARGADO DE BUSCAR UNA CUENTA POR SU NUMERO DE CUENTA
    public Cuenta buscarPorNumero(String numeroCuenta) {
        return cuentaDAO.buscarPorNumeroDeCuenta(numeroCuenta);
    }


    // MÉTODO ENCARGADO DE ACTUALIZAR LOS DATOS DE UNA CUENTA
    public void actualizar(Cuenta cuenta) {
        cuentaDAO.actualizar(cuenta);
    }
}