package com.example.Market_Data_Aggregator.repository;

import com.example.Market_Data_Aggregator.model.Holding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HoldingRepository extends JpaRepository<Holding, Long> {
}
