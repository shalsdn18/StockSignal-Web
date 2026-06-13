package com.stocksignal.controller;

import com.stocksignal.entity.StockSignal;
import com.stocksignal.service.StockSignalService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
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
     */
    @GetMapping("/dashboard")
    public String dashboard(
            @RequestParam(value = "ticker", required = false) String ticker,
            @RequestParam(value = "startDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(value = "endDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            Model model) {
        List<StockSignal> signals;

        // Determine which search path to take
        boolean hasTickerFilter = ticker != null && !ticker.isBlank();
        boolean hasDateFilter = startDate != null || endDate != null;

        if (hasTickerFilter && hasDateFilter) {
            signals = signalService.searchSignalsByTickerAndDateRange(ticker, startDate, endDate);
        } else if (hasTickerFilter) {
            signals = signalService.searchSignalsByTicker(ticker);
        } else if (hasDateFilter) {
            signals = signalService.searchSignalsByDateRange(startDate, endDate);
        } else {
            signals = signalService.getAllSignals();
        }

        long buyCount = signals.stream()
                .filter(s -> s.getSignalType().name().equals("BUY"))
                .count();
        long sellCount = signals.stream()
                .filter(s -> s.getSignalType().name().equals("SELL"))
                .count();

        model.addAttribute("signals", signals);
        model.addAttribute("totalCount", signals.size());
        model.addAttribute("buyCount", buyCount);
        model.addAttribute("sellCount", sellCount);

        return "dashboard";
    }
}
