package com.stocksignal.service;

import com.stocksignal.entity.SystemConfig;
import com.stocksignal.repository.SystemConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TossOpenApiService {

    private final RestTemplate restTemplate;
    private final SystemConfigRepository configRepository;

    private String cachedToken = null;
    private long tokenExpiryTime = 0;

    public synchronized String getAccessToken() {
        long currentTime = System.currentTimeMillis();
        if (cachedToken != null && currentTime < tokenExpiryTime) return cachedToken;

        SystemConfig config = configRepository.findById(1L).orElseGet(SystemConfig::new);
        if (config.getTossClientId() == null || config.getTossClientSecret() == null || config.getTossClientId().isBlank()) return null;

        String url = "https://openapi.tossinvest.com/oauth2/token";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("grant_type", "client_credentials");
        map.add("client_id", config.getTossClientId());
        map.add("client_secret", config.getTossClientSecret());

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, new HttpEntity<>(map, headers), Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                cachedToken = (String) response.getBody().get("access_token");
                Integer expiresIn = (Integer) response.getBody().get("expires_in");
                tokenExpiryTime = currentTime + ((expiresIn != null ? expiresIn : 3600) - 60) * 1000L;
                return cachedToken;
            }
        } catch (Exception e) {
            log.error("Toss Token 발급 예외: {}", e.getMessage());
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getCurrentHoldings() {
        String token = getAccessToken();
        SystemConfig config = configRepository.findById(1L).orElseGet(SystemConfig::new);
        if (token == null || config.getTossClientId() == null || config.getTossClientId().isBlank()) return Collections.emptyList();

        String url = "https://openapi.tossinvest.com/api/v1/holdings";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.set("X-Tossinvest-Account", config.getTossAccountSeq());
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (List<Map<String, Object>>) response.getBody().get("holdings");
            }
        } catch (Exception e) {
            log.error("Toss Holdings 연동 실패: {}", e.getMessage());
        }
        return Collections.emptyList();
    }
}
