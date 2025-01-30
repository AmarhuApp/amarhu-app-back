package com.amarhu.production.controller;

import com.amarhu.production.entity.Production;
import com.amarhu.production.service.ProductionLastMonthService;
import com.amarhu.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/production-last-month")
public class ProductionLastMonthController {

    @Autowired
    private ProductionLastMonthService productionLastMonthService;
    @Autowired
    private UserService userService;

    @GetMapping
    public List<Production> getProductionForLastMonth() {
        return productionLastMonthService.getProductionForLastMonth();
    }
}
