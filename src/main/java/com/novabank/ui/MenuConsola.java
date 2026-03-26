package com.novabank.ui;

import com.novabank.modelo.Cliente;
import com.novabank.modelo.Cuenta;
import com.novabank.modelo.Movimiento;
import com.novabank.repositorio.ClienteDAO;
import com.novabank.repositorio.CuentaDAO;
import com.novabank.servicio.ClienteService;
import com.novabank.servicio.CuentaService;
import com.novabank.servicio.OperacionService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class MenuConsola {

    // Inicializamos las herramientas como variables de la clase (ya no son estáticas)
    private Scanner scanner = new Scanner(System.in);
    private ClienteDAO clienteDAO = new ClienteDAO();
    private CuentaDAO cuentaDAO = new CuentaDAO();
    private ClienteService clienteService = new ClienteService(clienteDAO);
    private CuentaService cuentaService = new CuentaService(cuentaDAO, clienteService);
    private OperacionService operacionService = new OperacionService(memoria, cuentaService);

    // Este es el método que arrancará el bucle del menú
    public void iniciar() {
        boolean salir = false;

        while (!salir) {
            System.out.println("\n=== NOVABANK ===");
            System.out.println("SISTEMA DE OPERACIONES");
            System.out.println("1. Gestión de clientes");
            System.out.println("2. Gestión de cuentas");
            System.out.println("3. Operaciones financieras");
            System.out.println("4. Consultas");
            System.out.println("5. Salir");
            System.out.print("Seleccione una opción: ");

            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    menuClientes();
                    break;
                case "2":
                    menuCuentas();
                    break;
                case "3":
                    menuOperaciones();
                    break;
                case "4":
                    menuConsultas();
                    break;
                case "5":
                    salir = true;
                    System.out.println("Cerrando el sistema... ¡Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    // --- GESTIÓN DE CLIENTES ---
    private void menuClientes() {
        System.out.println("\n--- GESTIÓN DE CLIENTES ---");
        System.out.println("1. Crear cliente");
        System.out.println("2. Buscar cliente");
        System.out.println("3. Listar clientes");
        System.out.println("4. Volver");
        System.out.print("Seleccione una opción: ");

        String opcion = scanner.nextLine();

        try {
            switch (opcion) {
                case "1":
                    System.out.print("Nombre: ");
                    String nombre = scanner.nextLine();
                    System.out.print("Apellidos: ");
                    String apellidos = scanner.nextLine();
                    System.out.print("DNI: ");
                    String dni = scanner.nextLine();
                    System.out.print("Email: ");
                    String email = scanner.nextLine();
                    System.out.print("Teléfono: ");
                    String telefono = scanner.nextLine();

                    Cliente nuevo = clienteService.crearCliente(nombre, apellidos, dni, email, telefono);
                    System.out.println("Cliente creado correctamente. ID cliente: " + nuevo.getId());
                    break;
                case "2":
                    System.out.print("Introduzca el DNI a buscar: ");
                    String dniBusqueda = scanner.nextLine();
                    Cliente encontrado = clienteService.buscarPorDni(dniBusqueda);
                    if (encontrado != null) {
                        System.out.println("Cliente encontrado: " + encontrado.getNombre() + " " + encontrado.getApellidos() + " (ID: " + encontrado.getId() + ")");
                    } else {
                        System.out.println("ERROR: No se encontró ningún cliente.");
                    }
                    break;
                case "3":
                    List<Cliente> lista = clienteService.listarClientes();
                    if (lista.isEmpty()) {
                        System.out.println("No hay clientes registrados.");
                    } else {
                        for (Cliente c : lista) {
                            System.out.println("ID: " + c.getId() + " | Nombre: " + c.getNombre() + " | DNI: " + c.getDni());
                        }
                    }
                    break;
                case "4":
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // --- GESTIÓN DE CUENTAS ---
    private void menuCuentas() {
        System.out.println("\n--- GESTIÓN DE CUENTAS ---");
        System.out.println("1. Crear cuenta");
        System.out.println("2. Listar cuentas de cliente");
        System.out.println("3. Ver información de cuenta");
        System.out.println("4. Volver");
        System.out.print("Seleccione una opción: ");

        String opcion = scanner.nextLine();

        try {
            switch (opcion) {
                case "1":
                    System.out.print("ID del cliente titular: ");
                    Long idCliente = Long.parseLong(scanner.nextLine());
                    Cuenta nueva = cuentaService.crearCuenta(idCliente);
                    System.out.println("Cuenta creada correctamente. Número: " + nueva.getNumeroCuenta());
                    break;
                case "2":
                    System.out.print("ID del cliente: ");
                    Long idBusqueda = Long.parseLong(scanner.nextLine());
                    List<Cuenta> cuentas = cuentaService.listarCuentasDeCliente(idBusqueda);
                    if (cuentas.isEmpty()) {
                        System.out.println("Este cliente no tiene cuentas.");
                    } else {
                        for (Cuenta c : cuentas) {
                            System.out.println("Cuenta: " + c.getNumeroCuenta() + " | Saldo: " + c.getSaldo() + " €");
                        }
                    }
                    break;
                case "3":
                    System.out.print("Introduzca número de cuenta: ");
                    String numCuenta = scanner.nextLine();
                    Cuenta c = cuentaService.buscarPorNumero(numCuenta);
                    if (c != null) {
                        System.out.println("Número: " + c.getNumeroCuenta() + " | Saldo: " + c.getSaldo() + " €");
                    } else {
                        System.out.println("La cuenta no existe.");
                    }
                    break;
                case "4":
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Debe introducir un número válido.");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // --- OPERACIONES FINANCIERAS ---
    private void menuOperaciones() {
        System.out.println("\n--- OPERACIONES FINANCIERAS ---");
        System.out.println("1. Depositar dinero");
        System.out.println("2. Retirar dinero");
        System.out.println("3. Transferencia entre cuentas");
        System.out.println("4. Volver");
        System.out.print("Seleccione una opción: ");

        String opcion = scanner.nextLine();

        try {
            switch (opcion) {
                case "1":
                    System.out.print("Número de cuenta: ");
                    String cuentaDep = scanner.nextLine();
                    System.out.print("Cantidad a depositar: ");
                    BigDecimal cantDep = new BigDecimal(scanner.nextLine());
                    operacionService.depositar(cuentaDep, cantDep);
                    System.out.println("Depósito realizado correctamente.");
                    break;
                case "2":
                    System.out.print("Número de cuenta: ");
                    String cuentaRet = scanner.nextLine();
                    System.out.print("Cantidad a retirar: ");
                    BigDecimal cantRet = new BigDecimal(scanner.nextLine());
                    operacionService.retirar(cuentaRet, cantRet);
                    System.out.println("Retiro realizado correctamente.");
                    break;
                case "3":
                    System.out.print("Número de cuenta ORIGEN: ");
                    String origen = scanner.nextLine();
                    System.out.print("Número de cuenta DESTINO: ");
                    String destino = scanner.nextLine();
                    System.out.print("Cantidad a transferir: ");
                    BigDecimal cantTrans = new BigDecimal(scanner.nextLine());
                    operacionService.transferir(origen, destino, cantTrans);
                    System.out.println("Transferencia realizada correctamente.");
                    break;
                case "4":
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Formato de número incorrecto.");
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        }
    }

    // --- CONSULTAS ---
    private void menuConsultas() {
        System.out.println("\n--- CONSULTAS ---");
        System.out.println("1. Consultar saldo");
        System.out.println("2. Historial de movimientos");
        System.out.println("3. Movimientos en rango de fechas");
        System.out.println("4. Volver");
        System.out.print("Seleccione una opción: ");

        String opcion = scanner.nextLine();

        try {
            switch (opcion) {
                case "1":
                    System.out.print("Introduzca número de cuenta: ");
                    String numCuenta = scanner.nextLine();
                    Cuenta c = cuentaService.buscarPorNumero(numCuenta);
                    if (c != null) {
                        System.out.println("Saldo actual: " + c.getSaldo() + " €");
                    } else {
                        System.out.println("La cuenta no existe.");
                    }
                    break;
                case "2":
                    System.out.print("Introduzca número de cuenta: ");
                    String cuentaHist = scanner.nextLine();
                    List<Movimiento> historial = operacionService.obtenerHistorial(cuentaHist);
                    if (historial.isEmpty()) {
                        System.out.println("No hay movimientos para esta cuenta.");
                    } else {
                        for (Movimiento m : historial) {
                            System.out.println(m.getFecha().toLocalDate() + " | " + m.getTipo() + " | " + m.getCantidad() + " €");
                        }
                    }
                    break;

                case "3":
                    System.out.println("Introduzca la fecha de inicio (dd/MM/yyyy): ");
                    String fechaInicio = scanner.nextLine();
                    System.out.println("Introduzca la fecha de fin (dd/MM/yyyy): ");
                    String fechaFin = scanner.nextLine();

                    List<Movimiento> movPorFecha = operacionService.obtenerMovimientosRangoFecha(fechaInicio, fechaFin);
                    for(Movimiento m : movPorFecha) {
                        System.out.println(m.getFecha().toLocalDate() + " | " + m.getTipo() + " | " + m.getCantidad() + " €");
                    }
                    break;
                case "4":
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}