package com.example.Market_Data_Aggregator.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

// What you send when saving a coin. buyPrice is the price per coin in INR.
public record HoldingRequest(
        @NotBlank(message = "coin is required") String coin,
        @NotNull(message = "quantity is required")
        @DecimalMin(value = "0", inclusive = false, message = "value must be greater than 0")BigDecimal quantity,
        @NotNull(message = "buy price is required")
        @DecimalMin(value = "0", inclusive = false, message = "buyPrice must be greater than 0") BigDecimal buyPrice) {
}
