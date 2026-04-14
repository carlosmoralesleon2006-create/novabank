package com.novabank.modelo;

import java.time.LocalDateTime;

public class Cliente {
    private Long id; //Identificador único del cliente
    private final String nombre; //Nombre del cliente
    private final String apellidos; //Apellidos del cliente
    private final String dni; //Número de dni del cliente
    private final String email; //Correo electrónico del cliente
    private final String telefono; //Número telefonico del cliente
    private final LocalDateTime fechaCreacion; //Fecha registro del cliente

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



    public static class ClienteBuilder {
        private String nombre;
        private String apellidos;
        private String dni;
        private String email;
        private String telefono;

        public ClienteBuilder conNombre(String nombre) {
            this.nombre = nombre;
            return this; // Devuelve el propio builder para encadenar llamadas
        }

        public ClienteBuilder conApellidos(String apellidos) {
            this.apellidos = apellidos;
            return this;
        }

        public ClienteBuilder conDni(String dni) {
            this.dni = dni;
            return this;
        }

        public ClienteBuilder conEmail(String email) {
            this.email = email;
            return this;
        }

        public ClienteBuilder conTelefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public Cliente build() {
            return new Cliente(nombre, apellidos, dni, email, telefono);
        }
    }
}

