package com.amarhu.production.service;

import com.amarhu.production.dto.TopProducerDTO;
import com.amarhu.production.repository.ProductionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopProducersService {

    @Autowired
    private ProductionRepository productionRepository;

    public List<TopProducerDTO> getTopProducers() {
        // Obtén todas las producciones y mapea al formato del DTO
        return productionRepository.findAll().stream()
                .map(production -> {
                    TopProducerDTO dto = new TopProducerDTO();
                    dto.setId(production.getUser().getId());
                    dto.setName(production.getUser().getName());
                    dto.setVideoCount(production.getTotalVideos());
                    dto.setFallenVideos(production.getVideosCaidos());
                    dto.setNetEarnings(production.getGananciaNeta());
                    dto.setTotalProductionCost(production.getCosteTotalProduccion());
                    dto.setCodigo(production.getUser().getCodigo());
                    return dto;
                })
                .sorted((p1, p2) -> Integer.compare(p2.getVideoCount(), p1.getVideoCount())) // Ordenar por videoCount descendente
                .collect(Collectors.toList());
    }
}
