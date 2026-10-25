package com.example.Market_Data_Aggregator.exceptions;

import java.time.Instant;

public record ErrorResponse(Instant timeStamp, int status, String message) {
}
