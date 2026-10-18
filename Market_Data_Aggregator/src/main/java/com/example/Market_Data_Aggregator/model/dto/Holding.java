package com.example.Market_Data_Aggregator.model.dto;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "holding")
public class Holding {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "coin_id", nullable = false)
    private String coinId;

    @Column(nullable = false, precision = 38, scale = 18)
    private BigDecimal quantity;

    @Column(name = "buy_price", nullable = false, precision = 38, scale = 8)
    private BigDecimal buyPrice;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public Holding(){}

    public Holding(String coinId, BigDecimal quantity, BigDecimal buyPrice) {
        this.coinId = coinId;
        this.quantity = quantity;
        this.buyPrice = buyPrice;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCoinId() { return coinId; }
    public void setCoinId(String coinId) { this.coinId = coinId; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public BigDecimal getBuyPrice() { return buyPrice; }
    public void setBuyPrice(BigDecimal buyPrice) { this.buyPrice = buyPrice; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
