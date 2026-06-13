package com.stocksignal.controller;

import com.stocksignal.entity.MorningBriefing;
import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import com.stocksignal.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class TestUiController {

    @GetMapping("/test/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("signals", createSampleSignals());
        model.addAttribute("totalCount", 142);
        model.addAttribute("buyCount", 98);
        model.addAttribute("sellCount", 44);

        return "dashboard";
    }

    @GetMapping({"/login", "/test/login"})
    public String login() {
        return "login";
    }

    @PostMapping("/test/login-submit")
    public String submitLogin() {
        return "redirect:/login";
    }

    @GetMapping({"/register", "/test/register"})
    public String register() {
        return "register";
    }

    @PostMapping("/test/register-submit")
    public String submitRegister() {
        return "redirect:/register";
    }

    @GetMapping({"/briefing", "/test/briefing"})
    public String briefing(Model model) {
        MorningBriefing briefing = new MorningBriefing(
                "🚀 오늘의 AI 증시 요약",
                "<p>나스닥 <b>1.2% 상승</b></p>",
                "안정세"
        );

        model.addAttribute("briefing", briefing);
        model.addAttribute("totalCount", 142);
        model.addAttribute("buyCount", 98);
        model.addAttribute("sellCount", 44);

        return "briefing";
    }

    @GetMapping({"/settings", "/test/settings"})
    public String settings(Model model) {
        User user = new User(
                "shalsdn18",
                "encryptedPassword",
                "shalsdn18@hannam.ac.kr",
                "778899123",
                "123456789:ABCdefGhIJKlmNoPQ_TestToken"
        );

        model.addAttribute("user", user);
        model.addAttribute("totalCount", 142);
        model.addAttribute("buyCount", 98);
        model.addAttribute("sellCount", 44);

        return "settings";
    }

    @PostMapping("/test/settings-save")
    public String submitSettings() {
        return "redirect:/settings";
    }

    private List<StockSignal> createSampleSignals() {
        List<StockSignal> signals = new ArrayList<>();

        StockSignal appleBuy = new StockSignal("AAPL", SignalType.BUY, 182.50, "Golden cross confirmed");
        appleBuy.setCreatedAt(LocalDateTime.of(2026, 5, 24, 9, 15, 0));

        StockSignal teslaSell = new StockSignal("TSLA", SignalType.SELL, 198.40, "Resistance failed");
        teslaSell.setCreatedAt(LocalDateTime.of(2026, 5, 24, 10, 5, 0));

        StockSignal nvidiaBuy = new StockSignal("NVDA", SignalType.BUY, 946.20, "AI momentum continues");
        nvidiaBuy.setCreatedAt(LocalDateTime.of(2026, 5, 24, 10, 45, 0));

        signals.add(appleBuy);
        signals.add(teslaSell);
        signals.add(nvidiaBuy);

        return signals;
    }
}