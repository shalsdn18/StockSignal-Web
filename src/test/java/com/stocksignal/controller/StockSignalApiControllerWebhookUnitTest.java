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
        when(telegramSignalParser.parse("[SELL] TSLA 198.40")).thenReturn(request);
        when(signalService.createSignal(request)).thenReturn(saved);

        ResponseEntity<StockSignal> response = controller.receiveWebhook("[SELL] TSLA 198.40");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isSameAs(saved);
        verify(telegramSignalParser).parse("[SELL] TSLA 198.40");
        verify(signalService).createSignal(request);
    }

    @Test
    void receiveWebhook_returnsBadRequestWhenParsingFails() {
        when(telegramSignalParser.parse("invalid payload"))
                .thenThrow(new IllegalArgumentException("Unable to parse stock signal text: invalid payload"));

        ResponseEntity<StockSignal> response = controller.receiveWebhook("invalid payload");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}