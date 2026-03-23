package com.novabank.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Movimiento {
    private Long id;
    private String numeroCuenta;
    private TipoMovimiento tipo;
    private BigDecimal cantidad;
    private LocalDateTime fecha;

    public Movimiento(String numeroCuenta, TipoMovimiento tipo, BigDecimal cantidad) {
        this.numeroCuenta = numeroCuenta;
        this.tipo = tipo;
        this.cantidad = cantidad;
        this.fecha = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public TipoMovimiento getTipo() { return tipo; }
    public BigDecimal getCantidad() { return cantidad; }
    public LocalDateTime getFecha() { return fecha; }
}