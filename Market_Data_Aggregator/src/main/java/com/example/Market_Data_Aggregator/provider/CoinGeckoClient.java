package com.example.Market_Data_Aggregator.provider;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.util.Map;

@Component
public class CoinGeckoClient {

    private final RestClient restClient;

    public CoinGeckoClient(RestClient restClient){
        this.restClient = restClient;
    }

    public Map<String, Map<String, BigDecimal>> getPrices(String ids, String currency){
        return restClient.get()
                .uri(u -> u.path("/simple/price")
                        .queryParam("ids", ids)
                        .queryParam("vs_currencies", currency)
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
    }
}
