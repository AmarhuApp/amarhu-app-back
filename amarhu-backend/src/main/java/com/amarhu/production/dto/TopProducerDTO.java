package com.amarhu.production.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TopProducerDTO {
    private Long id;
    private String name;
    private int videoCount;
    private int fallenVideos;
    private BigDecimal netEarnings;
    private BigDecimal totalProductionCost;
    private String codigo; // Código del redactor
}
