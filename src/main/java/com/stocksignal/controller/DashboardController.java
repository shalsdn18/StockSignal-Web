package com.stocksignal.controller;

import com.stocksignal.dto.DashboardStatisticsDto;
import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import com.stocksignal.service.StockSignalService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Thymeleaf controller that serves the signal history dashboard.
 */
@Controller
public class DashboardController {

    private final StockSignalService signalService;

    public DashboardController(StockSignalService signalService) {
        this.signalService = signalService;
    }

    /**
     * Renders the service-backed dashboard page at {@code /dashboard}.
     * Supports dynamic filtering by ticker, date range, and signal type (all optional, AND-combined).
     */
    @GetMapping({"/", "/dashboard"})
    public String dashboard(
            @RequestParam(value = "ticker", required = false) String ticker,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "signalType", required = false) SignalType signalType,
            Model model) {
        // Normalize filters
        String normalizedTicker = (ticker != null && !ticker.isBlank()) ? ticker : null;

        // Unified dynamic search: all filters are optional and combined with AND logic
        List<StockSignal> signals = signalService.searchSignalsByDynamicFilters(
                normalizedTicker,
                startDate,
                endDate,
                signalType
        );

        // REQ-F-012: dashboard statistics across the entire archived dataset
        DashboardStatisticsDto stats = signalService.calculateOverallStatistics();
        LocalDateTime lastSignalReceivedAt = signalService.getLastSignalReceivedAt();

        model.addAttribute("signals", signals);
        model.addAttribute("dashboardStatistics", stats);
        model.addAttribute("lastSignalReceivedAt", lastSignalReceivedAt);
        model.addAttribute("statistics", stats);

        return "dashboard";
    }
}
