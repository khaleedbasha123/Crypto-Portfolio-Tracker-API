package com.example.Market_Data_Aggregator.model.dto;

import java.math.BigDecimal;

public record PortfolioItem(
        Long holdingId,
        String coin,
        BigDecimal Quantity,
        BigDecimal buyPrice,
        BigDecimal currentPrice,
        BigDecimal invested,
        BigDecimal currentValue,
        BigDecimal profitLoss,
        BigDecimal profitLossPercent) {
}
