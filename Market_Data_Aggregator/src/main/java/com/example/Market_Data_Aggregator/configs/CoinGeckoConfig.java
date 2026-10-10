package com.example.Market_Data_Aggregator.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class CoinGeckoConfig {

    @Bean
    public RestClient coinGeckoRestClient(
            @Value("${coingecko.base-url}") String baseUrl,
            @Value("${coingecko.api-key}") String apiKey,
            @Value("${coingecko.connect-timeout-ms}") int connectTimeout,
            @Value("${coingecko.read-timeout-ms}") int readTimeOut
    ){

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(connectTimeout);
        factory.setReadTimeout(readTimeOut);

        return RestClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("x_cg_demo_api_key", apiKey)
                .requestFactory(factory)
                .build();
    }
}
