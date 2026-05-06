package com.stocksignal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

/**
 * JPA entity representing a stock buy/sell signal.
 */
@Entity
@Table(name = "stock_signal")
public class StockSignal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Stock ticker symbol (e.g. AAPL, TSLA). */
    @NotBlank
    @Column(nullable = false, length = 20)
    private String ticker;

    /** Signal type: BUY or SELL. */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SignalType signalType;

    /** Price at which the signal was generated. */
    @NotNull
    @Positive
    @Column(nullable = false)
    private Double price;

    /** Optional message or reason for the signal. */
    @Column(length = 500)
    private String message;

    /** Timestamp when the signal was created. */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ---- Constructors ----

    public StockSignal() {
    }

    public StockSignal(String ticker, SignalType signalType, Double price, String message) {
        this.ticker = ticker;
        this.signalType = signalType;
        this.price = price;
        this.message = message;
    }

    // ---- Getters & Setters ----

    public Long getId() {
        return id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public SignalType getSignalType() {
        return signalType;
    }

    public void setSignalType(SignalType signalType) {
        this.signalType = signalType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
