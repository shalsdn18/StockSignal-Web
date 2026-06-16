package com.stocksignal.controller;

import com.stocksignal.service.TossApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
@RequiredArgsConstructor
public class TossPortfolioApiController {

    private final TossApiService tossApiService;

    @GetMapping("/holdings")
    public ResponseEntity<List<Map<String, Object>>> getPortfolioHoldings() {
        return ResponseEntity.ok(tossApiService.getCurrentHoldings());
    }
}
