package com.stocksignal.controller;

import com.stocksignal.dto.StockSignalRequest;
import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import com.stocksignal.service.StockSignalService;
import com.stocksignal.util.TelegramSignalParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StockSignalApiControllerWebhookUnitTest {

    @Mock
    private StockSignalService signalService;

    @Mock
    private TelegramSignalParser telegramSignalParser;

    @InjectMocks
    private StockSignalApiController controller;

    @Test
    void receiveWebhook_returnsCreatedWhenParsingSucceeds() {
        StockSignalRequest request = new StockSignalRequest();
        request.setTicker("TSLA");
        request.setSignalType(SignalType.SELL);
        request.setPrice(198.40);

        StockSignal saved = new StockSignal("TSLA", SignalType.SELL, 198.40, null);
        saved.setId(1L); // ID 검증을 위해 가상 주입

        when(telegramSignalParser.parse("[SELL] TSLA 198.40")).thenReturn(request);
        when(signalService.createSignal(request)).thenReturn(saved);

        // 1. 변수 수신 타입을 ResponseEntity<Object> 로 정정하여 컴파일 에러 해결
        ResponseEntity<Object> response = controller.receiveWebhook("[SELL] TSLA 198.40");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        
        // 2. 응답 데이터 본문이 Map<String, Object> 구조로 반환되므로 맵 필드 단위 매칭 검증으로 수정
        assertThat(response.getBody()).isInstanceOf(Map.class);
        Map<?, ?> bodyMap = (Map<?, ?>) response.getBody();
        assertThat(bodyMap.get("id")).isEqualTo(1L);
        assertThat(bodyMap.get("ticker")).isEqualTo("TSLA");
        assertThat(bodyMap.get("signalType")).isEqualTo("SELL");
        assertThat(bodyMap.get("price")).isEqualTo(198.40);

        verify(telegramSignalParser).parse("[SELL] TSLA 198.40");
        verify(signalService).createSignal(request);
    }

    @Test
    void receiveWebhook_returnsBadRequestWhenParsingFails() {
        when(telegramSignalParser.parse("invalid payload"))
                .thenThrow(new IllegalArgumentException("Unable to parse stock signal text: invalid payload"));

        // 변수 수신 타입을 ResponseEntity<Object> 로 정정하여 컴파일 에러 해결
        ResponseEntity<Object> response = controller.receiveWebhook("invalid payload");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}