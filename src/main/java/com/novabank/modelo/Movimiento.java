package com.novabank.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Movimiento {
    private Long id; //Identificador único del movimiento
    private String numeroCuenta; //Número de la cuenta asociada
    private TipoMovimiento tipo; //Tipo de movimiento sacado de la enumeración
    private BigDecimal cantidad; //Cantidad de dinero usada
    private LocalDateTime fecha; //Fecha de realización del movimiento

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