package com.amarhu.production.service;

import com.amarhu.production.dto.RankingJRDTO;
import com.amarhu.production.entity.Production;
import com.amarhu.user.entity.Role;
import com.amarhu.user.entity.User;
import com.amarhu.production.repository.ProductionRepository;
import com.amarhu.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RankingJRService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductionRepository productionRepository;

    public List<RankingJRDTO> getRankingJRs() {
        // Obtener usuarios con rol Jefe de Redacción y código JR
        List<User> jrs = userRepository.findByRoleAndCodigo(Role.JEFE_REDACCION, "JR");

        // Crear el ranking basado en las producciones
        return jrs.stream()
                .map(jr -> {
                    List<Production> productions = productionRepository.findByUser(jr);

                    int produccionTotal = productions.stream()
                            .mapToInt(Production::getTotalVideos)
                            .sum();

                    BigDecimal gananciasTotales = productions.stream()
                            .map(Production::getGananciaTotal)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    BigDecimal gananciasNetas = productions.stream()
                            .map(Production::getGananciaNeta)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    int caidos = productions.stream()
                            .mapToInt(Production::getVideosCaidos)
                            .sum();

                    BigDecimal gananciaPromedio = produccionTotal > 0
                            ? gananciasNetas.divide(BigDecimal.valueOf(produccionTotal), BigDecimal.ROUND_HALF_UP)
                            : BigDecimal.ZERO;

                    return new RankingJRDTO(
                            jr.getId(),
                            jr.getName(),
                            produccionTotal,
                            gananciasTotales,
                            gananciasNetas,
                            caidos,
                            gananciaPromedio
                    );
                })
                .sorted((a, b) -> Integer.compare(b.getProduccionTotal(), a.getProduccionTotal()))
                .collect(Collectors.toList());
    }
}
