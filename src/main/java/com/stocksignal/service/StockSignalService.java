package com.stocksignal.service;

import com.stocksignal.dto.StockSignalRequest;
import com.stocksignal.entity.SignalMemo;
import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import com.stocksignal.entity.User;
import com.stocksignal.repository.SignalMemoRepository;
import com.stocksignal.repository.StockSignalRepository;
import com.stocksignal.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Business-logic service for stock signals.
 */
@Service
@Transactional
public class StockSignalService {

    private static final String DEFAULT_MEMO_USERNAME = "memo-user";
    private static final String DEFAULT_MEMO_PASSWORD = "memo-password";
    private static final String DEFAULT_MEMO_EMAIL = "memo-user@stocksignal.local";

    private final StockSignalRepository repository;
    private final SignalMemoRepository signalMemoRepository;
    private final UserRepository userRepository;
    private final TelegramNotificationService telegramService;

    public StockSignalService(StockSignalRepository repository,
                              SignalMemoRepository signalMemoRepository,
                              UserRepository userRepository,
                              TelegramNotificationService telegramService) {
        this.repository = repository;
        this.signalMemoRepository = signalMemoRepository;
        this.userRepository = userRepository;
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
        telegramService.sendMessage(buildNotificationText(saved));

        return saved;
    }

    /**
     * Deletes a stock signal by its ID.
     */
    public void deleteSignal(Long id) {
        repository.deleteById(id);
    }

    /**
     * Saves a memo for the given signal using a persisted mock user when authentication is absent.
     */
    public void saveMemo(Long signalId, String content) {
        StockSignal stockSignal = repository.findById(signalId)
                .orElseThrow(() -> new IllegalArgumentException("Signal not found: " + signalId));

        String memoText = content != null ? content.trim() : "";
        if (memoText.isEmpty()) {
            throw new IllegalArgumentException("Memo content must not be blank");
        }

        User memoUser = userRepository.findByUsername(DEFAULT_MEMO_USERNAME)
                .orElseGet(this::createDefaultMemoUser);

        SignalMemo memo = new SignalMemo(memoText, stockSignal, memoUser);
        signalMemoRepository.save(memo);
    }

    /**
     * Returns all signals ordered by creation time (newest first).
     */
    @Transactional(readOnly = true)
    public List<StockSignal> getAllSignals() {
        return repository.findAllByOrderByCreatedAtDesc();
    }

    /**
     * Returns a signal by its ID, or empty if not found.
     */
    @Transactional(readOnly = true)
    public Optional<StockSignal> getSignalById(Long id) {
        return repository.findById(id);
    }

    /**
     * Searches signals whose ticker contains the given keyword, newest first.
     */
    @Transactional(readOnly = true)
    public List<StockSignal> searchSignalsByTicker(String keyword) {
        return repository.findByTickerContainingIgnoreCaseOrderByCreatedAtDesc(keyword);
    }

    /**
     * Searches signals within a date range, newest first.
     * Converts LocalDate to LocalDateTime for database queries.
     *
     * @param startDate date range start (or null for no lower bound)
     * @param endDate date range end (or null for no upper bound)
     * @return signals within the range, or all signals if both dates are null
     */
    @Transactional(readOnly = true)
    public List<StockSignal> searchSignalsByDateRange(LocalDate startDate, LocalDate endDate) {
        // Normalize dates: swap if endDate < startDate
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }

        LocalDateTime startDateTime = startDate != null
                ? startDate.atStartOfDay()
                : null;
        LocalDateTime endDateTime = endDate != null
                ? endDate.atTime(LocalTime.MAX)
                : null;

        if (startDateTime != null && endDateTime != null) {
            return repository.findByDateRangeOrderByCreatedAtDesc(startDateTime, endDateTime);
        } else if (startDateTime != null) {
            // Only start date provided: from start date to now
            return repository.findByDateRangeOrderByCreatedAtDesc(startDateTime, LocalDateTime.now());
        } else if (endDateTime != null) {
            // Only end date provided: from epoch to end date
            return repository.findByDateRangeOrderByCreatedAtDesc(LocalDateTime.MIN, endDateTime);
        } else {
            // No dates provided: return all signals
            return getAllSignals();
        }
    }

    /**
     * Searches signals by ticker keyword and date range, newest first.
     */
    @Transactional(readOnly = true)
    public List<StockSignal> searchSignalsByTickerAndDateRange(String keyword, LocalDate startDate, LocalDate endDate) {
        // Normalize dates: swap if endDate < startDate
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }

        LocalDateTime startDateTime = startDate != null
                ? startDate.atStartOfDay()
                : LocalDateTime.MIN;
        LocalDateTime endDateTime = endDate != null
                ? endDate.atTime(LocalTime.MAX)
                : LocalDateTime.now();

        return repository.findByTickerContainingAndDateRangeOrderByCreatedAtDesc(keyword, startDateTime, endDateTime);
    }

    /**
     * Searches signals with dynamic filters (ticker, date range, signal type).
     * All filters are optional and combined with AND logic at the database level.
     *
     * @param ticker ticker keyword filter (null means no filter)
     * @param startDate date range start (null means no lower bound)
     * @param endDate date range end (null means no upper bound)
     * @param signalType signal type filter (null means no filter)
     * @return filtered signals, newest first
     */
    @Transactional(readOnly = true)
    public List<StockSignal> searchSignalsByDynamicFilters(String ticker, LocalDate startDate, LocalDate endDate, SignalType signalType) {
        // Normalize dates: swap if endDate < startDate
        if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
            LocalDate temp = startDate;
            startDate = endDate;
            endDate = temp;
        }

        LocalDateTime startDateTime = startDate != null
                ? startDate.atStartOfDay()
                : null;
        LocalDateTime endDateTime = endDate != null
                ? endDate.atTime(LocalTime.MAX)
                : null;

        return repository.findByDynamicFiltersOrderByCreatedAtDesc(
                ticker,
                startDateTime,
                endDateTime,
                signalType
        );
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
        String message = signal.getMessage();
        if (message == null || message.isBlank()) {
            message = "-";
        }

        return String.format(
                "\uD83D\uDCC8 Stock Signal Alert%nTicker: %s%nType: %s%nPrice: $%.2f%nMessage: %s",
                signal.getTicker(),
                signal.getSignalType(),
                signal.getPrice(),
                message
        );
    }

    private User createDefaultMemoUser() {
        User user = new User(
                DEFAULT_MEMO_USERNAME,
                DEFAULT_MEMO_PASSWORD,
                DEFAULT_MEMO_EMAIL,
                null,
                null
        );
        return userRepository.save(user);
    }
}
