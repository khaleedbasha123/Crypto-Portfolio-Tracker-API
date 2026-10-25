package com.example.Market_Data_Aggregator.model.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record PortfolioResponse(
        String currency,
        BigDecimal totalInvested,
        BigDecimal totalValue,
        BigDecimal totalProfitLoss,
        BigDecimal totalProfitLossPercent,
        Instant asOf,
        List<PortfolioItem> holdings
) {
}
