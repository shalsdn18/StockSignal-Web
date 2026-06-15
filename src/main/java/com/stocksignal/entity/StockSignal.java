package com.stocksignal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

/**
 * JPA entity representing a stock buy/sell signal.
 */
@Entity
@Table(
    name = "stock_signal",
    indexes = {
        @Index(name = "idx_stock_signal_created_at", columnList = "created_at DESC"),
        @Index(name = "idx_stock_signal_ticker_created_at", columnList = "ticker, created_at DESC")
    }
)
public class StockSignal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Stock ticker symbol (e.g. AAPL, TSLA). */
    @NotBlank
    @Size(max = 20)
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
    @Size(max = 500)
    @Column(length = 500)
    private String message;

    /** Timestamp when the signal was created. */
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** Owner user for the signal. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /** Memo list associated with this signal. Latest memo is kept first for dashboard display. */
    @OneToMany(mappedBy = "stockSignal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("updatedAt DESC")
    private List<SignalMemo> memos = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ---- Constructors ----

    public StockSignal() {
    }

    public StockSignal(String ticker, SignalType signalType, Double price, String message) {
        this(ticker, signalType, price, message, null);
    }

    public StockSignal(String ticker, SignalType signalType, Double price, String message, User user) {
        this.ticker = ticker;
        this.signalType = signalType;
        this.price = price;
        this.message = message;
        this.user = user;
    }

    // ---- Getters & Setters ----

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<SignalMemo> getMemos() {
        return memos;
    }

    public void setMemos(List<SignalMemo> memos) {
        this.memos = memos;
    }
}
