package com.example.Market_Data_Aggregator.controllers;

import com.example.Market_Data_Aggregator.model.dto.PriceResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Market_Data_Aggregator.services.PriceService;

@RestController
@RequestMapping("/api/price")
public class PriceController {

    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/{coin}")
    public PriceResponse getPrice(@PathVariable String coin,
                                  @RequestParam(defaultValue = "inr") String currency) {
        return priceService.getPrice(coin, currency);
    }
}