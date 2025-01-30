package com.amarhu.service;

import com.amarhu.production.entity.Production;
import com.amarhu.production.service.ProductionLastMonthService;
import com.amarhu.user.entity.User;
import com.amarhu.user.repository.UserRepository;
import com.amarhu.video.repository.VideoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class ProductionLastMonthServiceTest {

    @Autowired
    private ProductionLastMonthService productionLastMonthService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testCalculateLastMonthProduction() {
        // Obtener un usuario existente
        User user = userRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new RuntimeException("No hay usuarios en la base de datos."));

        // Calcular producción del mes pasado
        Production production = productionLastMonthService.calculateLastMonthProduction(user);

        // Validar
        assertNotNull(production);
        System.out.println("Producción del mes pasado: " + production);
    }
}
