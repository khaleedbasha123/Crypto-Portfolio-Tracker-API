package com.example.Market_Data_Aggregator.services;

import com.example.Market_Data_Aggregator.model.dto.CoinSearchResult;
import com.example.Market_Data_Aggregator.provider.CoinGeckoClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;


@Service
public class SearchService {

    private final CoinGeckoClient client;

    public SearchService(CoinGeckoClient coinGeckoClient){
        this.client = coinGeckoClient;
    }

    // Turns "btc" into the id "bitcoin". Cached for 24 hours.
    @Cacheable(cacheNames = "search", key = "#query.trim().toLowerCase()")
    public List<CoinSearchResult> search(String query){
        if(query == null  || query.trim().length() < 2){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "q must be atleast 2 characters");
        }

        return client.search(query.trim()).stream()
                .limit(10)
                .map(SearchService::toResult)
                .toList();
    }

    private static CoinSearchResult toResult(Map<String, Object> coin){
        Object rank = coin.get("market_cap_rank");

        return new CoinSearchResult(
                String.valueOf(coin.get("id")),
                String.valueOf(coin.get("name")),
                String.valueOf(coin.get("symbol")),
                rank instanceof Number n ? n.intValue() : null
        );
    }
}
