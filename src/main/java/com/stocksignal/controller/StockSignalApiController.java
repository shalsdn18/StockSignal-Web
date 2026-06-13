package com.stocksignal.controller;

import com.stocksignal.dto.StockSignalRequest;
import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import com.stocksignal.service.StockSignalService;
import com.stocksignal.util.TelegramSignalParser;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST API controller for stock signals.
 *
 * <ul>
 *   <li>POST  /api/signals          – create a new signal</li>
 *   <li>GET   /api/signals          – list all signals</li>
 *   <li>GET   /api/signals/{id}     – get signal by ID</li>
 *   <li>GET   /api/signals/ticker/{ticker} – filter by ticker</li>
 *   <li>GET   /api/signals/type/{type}     – filter by BUY / SELL</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/signals")
public class StockSignalApiController {

    private final StockSignalService signalService;
    private final TelegramSignalParser telegramSignalParser;

    public StockSignalApiController(StockSignalService signalService,
                                    TelegramSignalParser telegramSignalParser) {
        this.signalService = signalService;
        this.telegramSignalParser = telegramSignalParser;
    }

    /** Create a new stock signal. */
    @PostMapping
    public ResponseEntity<StockSignal> createSignal(
            @Valid @RequestBody StockSignalRequest request) {
        StockSignal saved = signalService.createSignal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    /** Receive a plaintext Telegram webhook payload and persist it as a signal. */
    @PostMapping(value = "/webhook", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<StockSignal> receiveWebhook(@RequestBody String rawMessage) {
        try {
            StockSignalRequest request = telegramSignalParser.parse(rawMessage);
            StockSignal saved = signalService.createSignal(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    /** List all signals (newest first). */
    @GetMapping
    public ResponseEntity<List<StockSignal>> getAllSignals() {
        return ResponseEntity.ok(signalService.getAllSignals());
    }

    /** Get a single signal by its database ID. */
    @GetMapping("/{id}")
    public ResponseEntity<StockSignal> getSignalById(@PathVariable Long id) {
        return signalService.getSignalById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Delete a signal by its database ID. */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSignal(@PathVariable Long id) {
        signalService.deleteSignal(id);
        return ResponseEntity.noContent().build();
    }

    /** Save a memo for a specific signal. */
    @PostMapping(value = "/{id}/memo", consumes = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<Void> saveMemo(@PathVariable Long id, @RequestBody String memoContent) {
        signalService.saveMemo(id, memoContent);
        return ResponseEntity.ok().build();
    }

    /** Filter signals by ticker symbol. */
    @GetMapping("/ticker/{ticker}")
    public ResponseEntity<List<StockSignal>> getSignalsByTicker(
            @PathVariable String ticker) {
        return ResponseEntity.ok(signalService.getSignalsByTicker(ticker));
    }

    /** Filter signals by type (BUY or SELL). */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<StockSignal>> getSignalsByType(
            @PathVariable SignalType type) {
        return ResponseEntity.ok(signalService.getSignalsByType(type));
    }
}
