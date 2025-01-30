package com.amarhu.production.dto;

import java.math.BigDecimal;

public class RankingJRDTO {

    private Long id;
    private String nombre;
    private int produccionTotal;
    private BigDecimal gananciasTotales;
    private BigDecimal gananciasNetas;
    private int caidos;
    private BigDecimal gananciaPromedio;

    public RankingJRDTO(Long id, String nombre, int produccionTotal, BigDecimal gananciasTotales, BigDecimal gananciasNetas, int caidos, BigDecimal gananciaPromedio) {
        this.id = id;
        this.nombre = nombre;
        this.produccionTotal = produccionTotal;
        this.gananciasTotales = gananciasTotales;
        this.gananciasNetas = gananciasNetas;
        this.caidos = caidos;
        this.gananciaPromedio = gananciaPromedio;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getProduccionTotal() {
        return produccionTotal;
    }

    public void setProduccionTotal(int produccionTotal) {
        this.produccionTotal = produccionTotal;
    }

    public BigDecimal getGananciasTotales() {
        return gananciasTotales;
    }

    public void setGananciasTotales(BigDecimal gananciasTotales) {
        this.gananciasTotales = gananciasTotales;
    }

    public BigDecimal getGananciasNetas() {
        return gananciasNetas;
    }

    public void setGananciasNetas(BigDecimal gananciasNetas) {
        this.gananciasNetas = gananciasNetas;
    }

    public int getCaidos() {
        return caidos;
    }

    public void setCaidos(int caidos) {
        this.caidos = caidos;
    }

    public BigDecimal getGananciaPromedio() {
        return gananciaPromedio;
    }

    public void setGananciaPromedio(BigDecimal gananciaPromedio) {
        this.gananciaPromedio = gananciaPromedio;
    }
}
