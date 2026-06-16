package com.stocksignal.controller;

import com.stocksignal.service.TossOpenApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class TossAssetController {

    private final TossOpenApiService tossOpenApiService;

    @GetMapping("/holdings")
    public ResponseEntity<List<Map<String, Object>>> getTossAssets() {
        // 컴파일이 파괴된 fetchAssetDataAsync 대신 무결성이 입증된 신규 국장/해외 통합 인터페이스를 강제 바인딩 매핑함
        List<Map<String, Object>> holdings = tossOpenApiService.getCurrentHoldings();
        return ResponseEntity.ok(holdings);
    }
}
