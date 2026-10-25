package com.example.Market_Data_Aggregator.exceptions;

import org.jboss.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = Logger.getLogger(GlobalExceptionHandler.class);

    // Our own Errors (404 coin Not Found, 400 bad days value, ...)
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleStatus(ResponseStatusException ex){
        String message = ex.getMessage() != null?ex.getMessage() : "Request Failed";
        return build(ex.getStatusCode().value(), message);
    }

    // Validation failed on a request body (negative quantity, missing coin, ...)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex){
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.joining(";"));

        return build(400, message.isBlank() ? "Invalid Request" : message);
    }

    // Broken JSON, or text where a number should be
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleUnreadable(HttpMessageNotReadableException ex){
        return build(400, "Malformed JSON request");
    }

    // CoinGecko Answered with an Error Status
    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<ErrorResponse> handleUpstream(RestClientResponseException ex){
        int status = ex.getStatusCode().value();
        log.warn("Coingecko returned {" + status + ")");

        if(status == 404){
            return build(404, "Not Found at CoinGecko");
        }
        if(status == 429){
            return build(503, "Coingecko rate limit reached. Try again in a minute.");
        }

        return build(503, "Coingecko returned an error (" + status + ")");
    }

    // CoinGecko did not answer at all (timeout, no internet)
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnreachable(ResourceAccessException ex) {
        log.warn("Could not reach CoinGecko: {" + ex.getMessage() + ")");
        return build(503, "Could not reach CoinGecko (timeout or network problem)");
    }

    private ResponseEntity<ErrorResponse> build(int status, String message){
        return ResponseEntity.status(status).body(new ErrorResponse(Instant.now(), status, message));
    }
}
