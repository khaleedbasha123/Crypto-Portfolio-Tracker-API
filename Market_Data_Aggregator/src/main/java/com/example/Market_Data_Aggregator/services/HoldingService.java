package com.example.Market_Data_Aggregator.services;

import com.example.Market_Data_Aggregator.model.Holding;
import com.example.Market_Data_Aggregator.model.dto.HoldingRequest;
import com.example.Market_Data_Aggregator.model.dto.HoldingResponse;
import com.example.Market_Data_Aggregator.repository.HoldingRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class HoldingService {

    private final HoldingRepository repository;
    private final PriceService priceService;

    public HoldingService(HoldingRepository repository, PriceService priceService){
        this.repository = repository;
        this.priceService = priceService;
    }

    public HoldingResponse add(HoldingRequest request){
        String coin = request.coin().trim().toLowerCase();

        // Throws 404 if CoinGecko doesn't know this coin, so typos never get saved
        priceService.getPrice(coin, "inr");

        Holding saved = repository.save(new Holding(coin, request.quantity(), request.buyPrice()));
        return HoldingResponse.from(saved);
    }

    public List<HoldingResponse> list(){
        return repository.findAll().stream().map(HoldingResponse::from).toList();
    }

    public void delete(Long id){
        if(!repository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Holding Not Found: " + id);
        }

        repository.deleteById(id);
    }
}
