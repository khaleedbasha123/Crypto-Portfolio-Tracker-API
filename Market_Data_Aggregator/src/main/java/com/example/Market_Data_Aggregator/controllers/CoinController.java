package com.example.Market_Data_Aggregator.controllers;

import com.example.Market_Data_Aggregator.model.dto.AnalyticsResponse;
import com.example.Market_Data_Aggregator.model.dto.CoinSearchResult;
import com.example.Market_Data_Aggregator.services.AnalyticsService;
import com.example.Market_Data_Aggregator.services.SearchService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/coins")
public class CoinController {

    private final SearchService searchService;
    private final AnalyticsService analyticsService;

    public CoinController(SearchService searchService, AnalyticsService analyticsService) {
        this.searchService = searchService;
        this.analyticsService = analyticsService;
    }

    @GetMapping("/search")
    public List<CoinSearchResult> search(
            @RequestParam String q
    ){
        return searchService.search(q);
    }

    @GetMapping("/{id}/analytics")
    public AnalyticsResponse analytics(
            @PathVariable String id,
            @RequestParam(defaultValue = "30") int days
    ){
        return analyticsService.getAnalytics(id, days);
    }
}
