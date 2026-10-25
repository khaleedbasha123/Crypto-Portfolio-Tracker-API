package com.example.Market_Data_Aggregator.services;

import com.example.Market_Data_Aggregator.model.Holding;
import com.example.Market_Data_Aggregator.model.dto.PortfolioItem;
import com.example.Market_Data_Aggregator.model.dto.PortfolioResponse;
import com.example.Market_Data_Aggregator.repository.HoldingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PortfolioService {

    private static final String CURRENCY = "inr";
    private static final BigDecimal HUNDRED = BigDecimal.valueOf(100);

    private final HoldingRepository repository;
    private final PriceService priceService;

    public PortfolioService(HoldingRepository repository, PriceService priceService){
        this.repository = repository;
        this.priceService = priceService;
    }

    public PortfolioResponse getPortfolio(){
        List<Holding> holdings = repository.findAll();
        if(holdings.isEmpty()){
            BigDecimal zero = money(BigDecimal.ZERO);

            return new PortfolioResponse(CURRENCY, zero, zero, zero, zero, Instant.now(), List.of());
        }

        // One CALL to CoinGecko for all the coins you hold
        String ids = holdings.stream()
                .map(Holding::getCoinId)
                .distinct()
                .sorted()
                .collect(Collectors.joining(","));

        Map<String, Map<String, BigDecimal>> prices = priceService.getPricesInInr(ids);

        List<PortfolioItem> items = new ArrayList<>();
        BigDecimal totalInvested = BigDecimal.ZERO;
        BigDecimal totalValue = BigDecimal.ZERO;

        for (Holding h : holdings) {
            Map<String, BigDecimal> coinPrices = (prices == null) ? null : prices.get(h.getCoinId());
            if (coinPrices == null || coinPrices.get(CURRENCY) == null) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "No price available for " + h.getCoinId());
            }
            BigDecimal currentPrice = coinPrices.get(CURRENCY);
            BigDecimal invested = h.getBuyPrice().multiply(h.getQuantity());
            BigDecimal value = currentPrice.multiply(h.getQuantity());
            BigDecimal profitLoss = value.subtract(invested);

            items.add(new PortfolioItem(
                    h.getId(), h.getCoinId(), h.getQuantity(), h.getBuyPrice(), currentPrice,
                    money(invested), money(value), money(profitLoss), percent(profitLoss, invested)));

            totalInvested = totalInvested.add(invested);
            totalValue = totalValue.add(value);
        }

        BigDecimal totalProfitLoss = totalValue.subtract(totalInvested);
        return new PortfolioResponse(CURRENCY, money(totalInvested), money(totalValue),
                money(totalProfitLoss), percent(totalProfitLoss, totalInvested), Instant.now(), items);
    }

    private static BigDecimal money(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    private static BigDecimal percent(BigDecimal part, BigDecimal whole) {
        if (whole.signum() == 0) {
            return BigDecimal.ZERO.setScale(2);
        }
        return part.multiply(HUNDRED).divide(whole, 2, RoundingMode.HALF_UP);
    }
}
