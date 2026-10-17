package com.example.Market_Data_Aggregator.services;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import com.example.Market_Data_Aggregator.model.dto.PriceResponse;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.Market_Data_Aggregator.provider.CoinGeckoClient;

@Service
public class PriceService {

    private final CoinGeckoClient client;

    public PriceService(CoinGeckoClient client) {
        this.client = client;
    }

    // Cached for 60 seconds per coin + currency
    @Cacheable(cacheNames = "prices", key = "#coin.toLowerCase() + ':' + #currency.toLowerCase()")
    public PriceResponse getPrice(String coin, String currency) {
        String id = coin.toLowerCase();
        String cur = currency.toLowerCase();

        Map<String, Map<String, BigDecimal>> result = client.getPrices(id, cur);
        Map<String, BigDecimal> prices = (result == null) ? null : result.get(id);

        // CoinGecko returns an empty {} for a coin it doesn't know
        if (prices == null || !prices.containsKey(cur)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Coin not found: " + coin);
        }
        return new PriceResponse(id, cur, prices.get(cur), Instant.now());
    }
}