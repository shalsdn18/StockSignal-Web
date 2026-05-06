package com.stocksignal.service;

import com.stocksignal.dto.StockSignalRequest;
import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import com.stocksignal.repository.StockSignalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Business-logic service for stock signals.
 */
@Service
@Transactional
public class StockSignalService {

    private final StockSignalRepository repository;
    private final TelegramNotificationService telegramService;

    public StockSignalService(StockSignalRepository repository,
                              TelegramNotificationService telegramService) {
        this.repository = repository;
        this.telegramService = telegramService;
    }

    /**
     * Persists a new stock signal and dispatches a Telegram notification.
     *
     * @param request validated request DTO
     * @return the saved {@link StockSignal}
     */
    public StockSignal createSignal(StockSignalRequest request) {
        StockSignal signal = new StockSignal(
                request.getTicker().toUpperCase(),
                request.getSignalType(),
                request.getPrice(),
                request.getMessage()
        );
        StockSignal saved = repository.save(signal);

        String notificationText = buildNotificationText(saved);
        telegramService.sendMessage(notificationText);

        return saved;
    }

    /**
     * Returns all signals ordered by creation time (newest first).
     */
    @Transactional(readOnly = true)
    public List<StockSignal> getAllSignals() {
        return repository.findAll()
                .stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .toList();
    }

    /**
     * Returns a signal by its ID, or empty if not found.
     */
    @Transactional(readOnly = true)
    public Optional<StockSignal> getSignalById(Long id) {
        return repository.findById(id);
    }

    /**
     * Returns signals for a specific ticker (newest first).
     */
    @Transactional(readOnly = true)
    public List<StockSignal> getSignalsByTicker(String ticker) {
        return repository.findByTickerIgnoreCaseOrderByCreatedAtDesc(ticker);
    }

    /**
     * Returns signals of a specific type (BUY / SELL), newest first.
     */
    @Transactional(readOnly = true)
    public List<StockSignal> getSignalsByType(SignalType signalType) {
        return repository.findBySignalTypeOrderByCreatedAtDesc(signalType);
    }

    /**
     * Returns the ten most recent signals for the dashboard summary.
     */
    @Transactional(readOnly = true)
    public List<StockSignal> getRecentSignals() {
        return repository.findTop10ByOrderByCreatedAtDesc();
    }

    // ---- helpers ----

    private String buildNotificationText(StockSignal signal) {
        StringBuilder sb = new StringBuilder();
        sb.append("\uD83D\uDCC8 Stock Signal Alert\n");
        sb.append("Ticker: ").append(signal.getTicker()).append("\n");
        sb.append("Type:   ").append(signal.getSignalType()).append("\n");
        sb.append("Price:  $").append(signal.getPrice()).append("\n");
        if (signal.getMessage() != null && !signal.getMessage().isBlank()) {
            sb.append("Note:   ").append(signal.getMessage());
        }
        return sb.toString();
    }
}
