package com.example.Market_Data_Aggregator.provider;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CoinGeckoClient {

    private final RestClient restClient;

    public CoinGeckoClient(RestClient restClient){
        this.restClient = restClient;
    }

    // Example result: {"bitcoin": {"inr": 5350000}}
    public Map<String, Map<String, BigDecimal>> getPrices(String ids, String currency){
        return restClient.get()
                .uri(u -> u.path("/simple/price")
                        .queryParam("ids", ids)
                        .queryParam("vs_currencies", currency)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }

    // Returns the "coins" list from /search, each item is a map with id, name, symbol, market_cap_rank
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> search(String query) {
        Map<String, Object> body = restClient.get()
                .uri(u -> u.path("/search").queryParam("query", query).build())
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});
        if (body == null || !(body.get("coins") instanceof List<?> coins)) {
            return List.of();
        }
        return (List<Map<String, Object>>) coins;
    }

    // Returns [[timestampMillis, price], ...] from /coins/{id}/market_chart
    public List<List<Number>> getMarketChart(String id, String currency, int days){
        Map<String, Object> body = restClient.get()
                .uri(u -> u.path("/coins/{id}/market_chart")
                            .queryParam("vs_currency", currency)
                            .queryParam("days", days)
                            .build(id))
                .retrieve()
                .body(new ParameterizedTypeReference<Map<String, Object>>() {});

        List<List<Number>> result = new ArrayList<>();
        if(body == null || !(body.get("prices") instanceof List<?> prices)){
            return result;
        }

        for (Object row : prices) {
            if (row instanceof List<?> r && r.size() >= 2
                    && r.get(0) instanceof Number t && r.get(1) instanceof Number p) {
                result.add(List.of(t, p));
            }
        }

        return result;
    }
}
