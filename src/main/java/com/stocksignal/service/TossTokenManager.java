package com.stocksignal.service;

import com.stocksignal.entity.SystemConfig;
import com.stocksignal.repository.SystemConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossTokenManager {
    private final RestTemplate restTemplate;
    private final SystemConfigRepository configRepository;
    
    private String cachedToken = null;
    private String autoAccountSeq = null;
    private long tokenExpiryTime = 0;

    public synchronized void clearCache() {
        this.cachedToken = null;
        this.autoAccountSeq = null;
        this.tokenExpiryTime = 0;
        log.info("🧹 [TossTokenManager] 공유 인메모리 토큰 및 계좌 캐시 초기화 완료.");
    }

    public synchronized String getAutoAccountSeq() {
        if (autoAccountSeq == null) { updateTokenAndAccount(); }
        return autoAccountSeq;
    }

    public synchronized String getAccessToken() {
        if (cachedToken != null && System.currentTimeMillis() < tokenExpiryTime && autoAccountSeq != null) {
            return cachedToken;
        }
        updateTokenAndAccount();
        return cachedToken;
    }

    @SuppressWarnings("unchecked")
    private void updateTokenAndAccount() {
        long currentTime = System.currentTimeMillis();
        SystemConfig config = configRepository.findById(1L).orElseGet(SystemConfig::new);
        String clientId = config.getTossClientId() != null ? config.getTossClientId().trim() : "";
        String clientSecret = config.getTossClientSecret() != null ? config.getTossClientSecret().trim() : "";
        if (clientId.isBlank() || clientSecret.isBlank()) return;

        String url = "https://openapi.tossinvest.com/oauth2/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        map.add("client_id", clientId);
        map.add("client_secret", clientSecret);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, new HttpEntity<>(map, headers), Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().containsKey("access_token")) {
                String token = (String) response.getBody().get("access_token");
                
                HttpHeaders acctHeaders = new HttpHeaders();
                acctHeaders.setBearerAuth(token);
                ResponseEntity<Map> acctResp = restTemplate.exchange("https://openapi.tossinvest.com/api/v1/accounts", HttpMethod.GET, new HttpEntity<>(acctHeaders), Map.class);
                
                if (acctResp.getBody() != null && acctResp.getBody().containsKey("result")) {
                    List<Map<String, Object>> list = (List<Map<String, Object>>) acctResp.getBody().get("result");
                    if (!list.isEmpty()) {
                        this.autoAccountSeq = String.valueOf(list.get(0).get("accountSeq"));
                        this.cachedToken = token;
                        Integer expiresIn = (Integer) response.getBody().get("expires_in");
                        this.tokenExpiryTime = currentTime + ((expiresIn != null ? expiresIn : 3600) - 60) * 1000L;
                        log.info("🤖 [TossTokenManager] 통합 토큰 및 계좌번호 [{}] 자동 동적 바인딩 성공.", this.autoAccountSeq);
                    }
                }
            }
        } catch (Exception e) {
            log.error("🚨 [TossTokenManager] 인증 파이프라인 갱신 실패: {}", e.getMessage());
        }
    }
}