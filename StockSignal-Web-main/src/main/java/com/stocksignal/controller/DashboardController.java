package com.stocksignal.controller;

import com.stocksignal.entity.StockSignal;
import com.stocksignal.service.StockSignalService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
     * Renders the main dashboard page at {@code /} and {@code /dashboard}.
     */
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model) {
        List<StockSignal> allSignals = signalService.getAllSignals();
        List<StockSignal> recentSignals = signalService.getRecentSignals();

        long buyCount = allSignals.stream()
                .filter(s -> s.getSignalType().name().equals("BUY"))
                .count();
        long sellCount = allSignals.stream()
                .filter(s -> s.getSignalType().name().equals("SELL"))
                .count();

        model.addAttribute("signals", allSignals);
        model.addAttribute("recentSignals", recentSignals);
        model.addAttribute("totalCount", allSignals.size());
        model.addAttribute("buyCount", buyCount);
        model.addAttribute("sellCount", sellCount);

        return "dashboard";
    }
}
