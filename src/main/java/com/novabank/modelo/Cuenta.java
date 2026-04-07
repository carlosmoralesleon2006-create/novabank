package com.novabank.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Cuenta {
    private Long id; //Identificador único de la cuenta
    private String numeroCuenta; //Número unico de la cuenta
    private Long clienteId; //Identificador del cliente asociado a la cuenta
    private BigDecimal saldo; //Cantidad de dinero que posee la cuenta
    private LocalDateTime fechaCreacion; //Fecha en la que creó la cuenta

    public Cuenta(String numeroCuenta, Long clienteId) {
        this.numeroCuenta = numeroCuenta;
        this.clienteId = clienteId;
        this.saldo = BigDecimal.ZERO;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public long getClienteId() { return clienteId; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion){ this.fechaCreacion = fechaCreacion; }
}