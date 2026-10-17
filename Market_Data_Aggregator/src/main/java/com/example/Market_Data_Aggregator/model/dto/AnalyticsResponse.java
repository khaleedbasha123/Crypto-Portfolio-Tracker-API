package com.example.Market_Data_Aggregator.model.dto;

import java.time.Instant;
import java.util.List;

public record AnalyticsResponse(
        String coin,
        String currency,
        int days,
        int dailyPoints,
        double latestPrice,
        double movingAverage7d,
        double averageDailyReturnPercent,
        double volatilityPercent,
        double maxDrawdownPercent,
        List<Double> dailyReturnsPercent,
        Instant asOf) {
}
