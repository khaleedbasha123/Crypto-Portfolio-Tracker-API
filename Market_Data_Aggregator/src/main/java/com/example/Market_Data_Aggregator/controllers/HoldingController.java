package com.example.Market_Data_Aggregator.controllers;

import com.example.Market_Data_Aggregator.model.dto.HoldingRequest;
import com.example.Market_Data_Aggregator.model.dto.HoldingResponse;
import com.example.Market_Data_Aggregator.services.HoldingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/holdings")
public class HoldingController {

    private final HoldingService holdingService;

    public HoldingController(HoldingService holdingService){
        this.holdingService = holdingService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HoldingResponse add(@Valid @RequestBody HoldingRequest request){
        return holdingService.add(request);
    }

    @GetMapping
    public List<HoldingResponse> list(){
        return holdingService.list();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id){
        holdingService.delete(id);
    }
}
