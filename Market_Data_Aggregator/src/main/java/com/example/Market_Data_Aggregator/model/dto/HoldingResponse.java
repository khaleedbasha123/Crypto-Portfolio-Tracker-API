package com.example.Market_Data_Aggregator.model.dto;

import com.example.Market_Data_Aggregator.model.Holding;

import java.math.BigDecimal;
import java.time.Instant;

public record HoldingResponse(Long id, String coin, BigDecimal quantity, BigDecimal buyPrice, Instant createdAt) {

    public static HoldingResponse from(Holding h){
        return new HoldingResponse(h.getId(), h.getCoinId(), h.getQuantity(), h.getBuyPrice(), h.getCreatedAt());
    }
}
