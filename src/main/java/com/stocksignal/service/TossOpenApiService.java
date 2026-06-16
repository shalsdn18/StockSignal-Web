package com.stocksignal.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossOpenApiService {
    private final RestTemplate restTemplate;
    private final TossTokenManager tossTokenManager;

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCurrentHoldings() {
        String token = tossTokenManager.getAccessToken();
        String accountSeq = tossTokenManager.getAutoAccountSeq();
        if (token == null || accountSeq == null) return Collections.emptyList();

        String url = "https://openapi.tossinvest.com/api/v1/holdings";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("X-Tossinvest-Account", accountSeq);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // 1. 토스 응답 최상위 객체에서 "result" 맵을 먼저 추출
                Map<String, Object> resultObj = (Map<String, Object>) response.getBody().get("result");
                
                // 2. "result" 내부에 진짜 주식 배열인 "items"가 존재하면 파싱하여 리턴
                if (resultObj != null && resultObj.containsKey("items")) {
                    return (List<Map<String, Object>>) resultObj.get("items");
                }
            }
        } catch (Exception e) {
            log.error("🚨 [TossOpenApiService] holdings 수집 실패: {}", e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("401")) {
                tossTokenManager.clearCache();
            }
        }
        return Collections.emptyList();
    }
}