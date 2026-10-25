package com.example.Market_Data_Aggregator.services;

import com.example.Market_Data_Aggregator.model.dto.AnalyticsResponse;
import com.example.Market_Data_Aggregator.provider.CoinGeckoClient;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyticsService {

    private static final String CURRENCY = "inr";

    private final CoinGeckoClient client;

    public AnalyticsService(CoinGeckoClient coinGeckoClient){
        this.client = coinGeckoClient;
    }

    //Cached for 30 minutes per coin + days
    @Cacheable(cacheNames = "charts", key = "#coin.toLowerCase() + ':' + #days")
    public AnalyticsResponse getAnalytics(String coin, int days){
        if(days < 2 || days > 365){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "days must be between 2 and 365");
        }

        String id = coin.toLowerCase();
        List<Double> closes = dailyCloses(client.getMarketChart(id, CURRENCY, days));
        if(closes.size() < 2){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough price data for " + coin);
        }

        return calculate(id, days, closes);
    }

    // Keeps the last price of each UTC day (data may be hourly or 5-minute)
    static List<Double> dailyCloses(List<List<Number>> points) {
        Map<LocalDate, Double> byDay = new LinkedHashMap<>();
        for (List<Number> p : points) {
            LocalDate day = Instant.ofEpochMilli(p.get(0).longValue()).atZone(ZoneOffset.UTC).toLocalDate();
            byDay.put(day, p.get(1).doubleValue());
        }
        return new ArrayList<>(byDay.values());
    }

    static AnalyticsResponse calculate(String coin, int days, List<Double> closes) {
        // Daily returns in percent
        List<Double> returns = new ArrayList<>();
        for (int i = 1; i < closes.size(); i++) {
            returns.add((closes.get(i) / closes.get(i - 1) - 1) * 100);
        }
        double avgReturn = returns.stream().mapToDouble(Double::doubleValue).average().orElse(0);

        // Volatility = standard deviation of the daily returns (sample)
        double volatility = 0;
        if (returns.size() > 1) {
            double sumSq = 0;
            for (double r : returns) {
                sumSq += (r - avgReturn) * (r - avgReturn);
            }
            volatility = Math.sqrt(sumSq / (returns.size() - 1));
        }

        // Simple moving average of the last 7 daily closes (or fewer if not available)
        int window = Math.min(7, closes.size());
        double sma = closes.subList(closes.size() - window, closes.size()).stream()
                .mapToDouble(Double::doubleValue).average().orElse(0);

        // Max drawdown = biggest fall from a previous high, in percent (negative number)
        double peak = closes.get(0);
        double maxDrawdown = 0;
        for (double c : closes) {
            peak = Math.max(peak, c);
            maxDrawdown = Math.min(maxDrawdown, (c - peak) / peak * 100);
        }

        return new AnalyticsResponse(coin, CURRENCY, days, closes.size(),
                round(closes.get(closes.size() - 1)), round(sma), round(avgReturn),
                round(volatility), round(maxDrawdown),
                returns.stream().map(AnalyticsService::round).toList(), Instant.now());
    }

    private static double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }
}
