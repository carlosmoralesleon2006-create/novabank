package com.novabank.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Cuenta {
    private Long id;
    private String numeroCuenta;
    private Long clienteId;
    private BigDecimal saldo;
    private LocalDateTime fechaCreacion;

    public Cuenta(String numeroCuenta, Long clienteId) {
        this.numeroCuenta = numeroCuenta;
        this.clienteId = clienteId;
        this.saldo = BigDecimal.ZERO;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public Long getClienteId() { return clienteId; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
}