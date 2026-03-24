package com.novabank.modelo;

import java.time.LocalDateTime;

public class Cliente {
    private Long id; //Identificador único del cliente
    private String nombre; //Nombre del cliente
    private String apellidos; //Apellidos del cliente
    private String dni; //Número de dni del cliente
    private String email; //Correo electrónico del cliente
    private String telefono; //Número telefonico del cliente
    private LocalDateTime fechaCreacion; //Fecha registro del cliente

    public Cliente(String nombre, String apellidos, String dni, String email, String telefono) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.email = email;
        this.telefono = telefono;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getDni() { return dni; }
    public String getEmail() { return email; }
    public String getTelefono() { return telefono; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}