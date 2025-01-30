package com.amarhu.production.controller;

import com.amarhu.production.entity.Production;
import com.amarhu.production.service.ProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production")
public class ProductionController {

    @Autowired
    private ProductionService productionService;

    @GetMapping
    public List<Production> getAllProductions() {
        return productionService.getAllProductions();
    }
}
