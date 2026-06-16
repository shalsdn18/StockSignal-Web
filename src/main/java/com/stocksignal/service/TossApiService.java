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
public class TossApiService {
    private final RestTemplate restTemplate;
    private final TossTokenManager tossTokenManager;

    /**
     * 1. 실시간 보유 주식 잔고 조회 (ASSET 그룹 - 계좌 헤더 필수)
     */
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
                Map<String, Object> resultObj = (Map<String, Object>) response.getBody().get("result");
                if (resultObj != null && resultObj.containsKey("items")) {
                    return (List<Map<String, Object>>) resultObj.get("items");
                }
            }
        } catch (Exception e) {
            log.error("🚨 [TossApiService] holdings 수집 실패: {}", e.getMessage());
            if (e.getMessage() != null && e.getMessage().contains("401")) {
                tossTokenManager.clearCache();
            }
        }
        return Collections.emptyList();
    }

    /**
     * 2. 실시간 현재가 다건 조회 (MARKET_DATA 그룹 - 계좌 헤더 불필요)
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getStockPrices(String symbols) {
        String token = tossTokenManager.getAccessToken();
        if (token == null || symbols == null || symbols.isBlank()) return Collections.emptyList();

        String url = "https://openapi.tossinvest.com/api/v1/prices?symbols=" + symbols;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (List<Map<String, Object>>) response.getBody().get("result");
            }
        } catch (Exception e) {
            log.error("🚨 [TossApiService] 현재가 시세 조회 실패: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * 3. 차트용 캔들 데이터 조회 (MARKET_DATA_CHART 그룹 - 계좌 헤더 불필요)
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getStockCandles(String symbol, String interval, int count) {
        String token = tossTokenManager.getAccessToken();
        if (token == null) return Collections.emptyList();

        String url = String.format("https://openapi.tossinvest.com/api/v1/candles?symbol=%s&interval=%s&count=%d&adjusted=true", 
                symbol, interval, count);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                Map<String, Object> resultObj = (Map<String, Object>) response.getBody().get("result");
                if (resultObj != null && resultObj.containsKey("candles")) {
                    return (List<Map<String, Object>>) resultObj.get("candles");
                }
            }
        } catch (Exception e) {
            log.error("🚨 [TossApiService] 캔들 차트 데이터 조회 실패: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * 4. 종목 기본 정보 다건 조회 (STOCK 그룹 - 계좌 헤더 불필요)
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getStockInfo(String symbols) {
        String token = tossTokenManager.getAccessToken();
        if (token == null || symbols == null || symbols.isBlank()) return Collections.emptyList();

        String url = "https://openapi.tossinvest.com/api/v1/stocks?symbols=" + symbols;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (List<Map<String, Object>>) response.getBody().get("result");
            }
        } catch (Exception e) {
            log.error("🚨 [TossApiService] 종목 기본 정보 조회 실패: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * 5. 종목 매수 유의사항 및 VI 발동 정보 조회 (STOCK 그룹 - 계좌 헤더 불필요)
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getStockWarnings(String symbol) {
        String token = tossTokenManager.getAccessToken();
        if (token == null || symbol == null || symbol.isBlank()) return Collections.emptyList();

        String url = String.format("https://openapi.tossinvest.com/api/v1/stocks/%s/warnings", symbol);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (List<Map<String, Object>>) response.getBody().get("result");
            }
        } catch (Exception e) {
            log.error("🚨 [TossApiService] 매수 유의사항 조회 실패: {}", e.getMessage());
        }
        return Collections.emptyList();
    }

    /**
     * 6. 실시간 환율 정보 조회 (MARKET_INFO 그룹 - 계좌 헤더 불필요)
     * @param base 퀴리 기준 통화 (예: "USD")
     * @param quote 표시 통화 규격 (예: "KRW")
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getExchangeRate(String base, String quote) {
        String token = tossTokenManager.getAccessToken();
        if (token == null || base == null || quote == null) return Collections.emptyMap();

        String url = String.format("https://openapi.tossinvest.com/api/v1/exchange-rate?baseCurrency=%s&quoteCurrency=%s", base, quote);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (Map<String, Object>) response.getBody().get("result");
            }
        } catch (Exception e) {
            log.error("🚨 [TossApiService] 환율 조회 실패: {}", e.getMessage());
        }
        return Collections.emptyMap();
    }

    /**
     * 7. 국내(KRX+NXT) 장 운영 달력 조회 (MARKET_INFO 그룹 - 계좌 헤더 불필요)
     * @param date 조회 기준일 (YYYY-MM-DD 형식)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getKoreaMarketCalendar(String date) {
        String token = tossTokenManager.getAccessToken();
        if (token == null || date == null || date.isBlank()) return Collections.emptyMap();

        String url = "https://openapi.tossinvest.com/api/v1/market-calendar/KR?date=" + date;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (Map<String, Object>) response.getBody().get("result");
            }
        } catch (Exception e) {
            log.error("🚨 [TossApiService] 국내 마켓 캘린더 조회 실패: {}", e.getMessage());
        }
        return Collections.emptyMap();
    }

    /**
     * 8. 미국(USA) 장 운영 달력 조회 (MARKET_INFO 그룹 - 계좌 헤더 불필요)
     * @param date 조회 기준일 (미국 현지 날짜 YYYY-MM-DD 형식)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getUSAMarketCalendar(String date) {
        String token = tossTokenManager.getAccessToken();
        if (token == null || date == null || date.isBlank()) return Collections.emptyMap();

        String url = "https://openapi.tossinvest.com/api/v1/market-calendar/US?date=" + date;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return (Map<String, Object>) response.getBody().get("result");
            }
        } catch (Exception e) {
            log.error("🚨 [TossApiService] 미국 마켓 캘린더 조회 실패: {}", e.getMessage());
        }
        return Collections.emptyMap();
    }
}