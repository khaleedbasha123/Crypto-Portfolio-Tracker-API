package com.example.Market_Data_Aggregator.controllers;

import com.example.Market_Data_Aggregator.model.dto.PortfolioResponse;
import com.example.Market_Data_Aggregator.services.PortfolioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService){
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public PortfolioResponse get(){
        return portfolioService.getPortfolio();
    }

}
