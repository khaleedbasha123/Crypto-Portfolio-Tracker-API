package com.example.Market_Data_Aggregator.model.dto;

import java.math.BigDecimal;
import java.time.Instant;

public record PriceResponse(String coin, String currency, BigDecimal price, Instant asOf) {}