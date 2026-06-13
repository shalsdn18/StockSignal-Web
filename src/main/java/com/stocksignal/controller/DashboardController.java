package com.stocksignal.controller;

import com.stocksignal.entity.StockSignal;
import com.stocksignal.service.StockSignalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String dashboard(@RequestParam(value = "ticker", required = false) String ticker,
                            Model model) {
        List<StockSignal> signals = (ticker != null && !ticker.isBlank())
                ? signalService.searchSignalsByTicker(ticker)
                : signalService.getAllSignals();

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
