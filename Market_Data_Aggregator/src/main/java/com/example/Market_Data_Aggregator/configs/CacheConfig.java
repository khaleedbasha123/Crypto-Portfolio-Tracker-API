package com.example.Market_Data_Aggregator.configs;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final String PRICES = "prices";
    private static final String CHARTS = "charts";
    private static final String SEARCH = "search";

    @Bean
    public CacheManager cacheManager(){

        CaffeineCacheManager manager = new CaffeineCacheManager();

        //Prices Changes Fast, keep them for 60 Seconds
        manager.registerCustomCache(PRICES,
                Caffeine.newBuilder().expireAfterWrite(Duration.ofSeconds(60)).maximumSize(500).build());

        //Chart History Changes Slowly: 30 minutes
        manager.registerCustomCache(CHARTS,
                Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(30)).maximumSize(200).build());

        //Coin names almost never change: 24 hours

        manager.registerCustomCache(SEARCH,
                Caffeine.newBuilder().expireAfterWrite(Duration.ofHours(24)).maximumSize(500).build());

        return manager;
    }
}
