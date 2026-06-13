package com.stocksignal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class TelegramNotificationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    private TelegramNotificationService service;

    @BeforeEach
    void setUp() {
        service = new TelegramNotificationService(restTemplate);
    }

    @Test
    void sendMessage_doesNotCallApiWhenConfigurationIsMissing() {
        ReflectionTestUtils.setField(service, "botToken", "");
        ReflectionTestUtils.setField(service, "chatId", "123456");

        service.sendMessage("Signal ready");

        verifyNoInteractions(restTemplate);
    }

    @Test
    void sendMessage_callsTelegramApiWhenConfigured() {
        ReflectionTestUtils.setField(service, "botToken", "test-token");
        ReflectionTestUtils.setField(service, "chatId", "123456");

        service.sendMessage("Buy AAPL at 182.50");

        String expectedUrl = "https://api.telegram.org/bottest-token/sendMessage?chat_id=123456&text="
                + URLEncoder.encode("Buy AAPL at 182.50", StandardCharsets.UTF_8);
        verify(restTemplate).getForObject(expectedUrl, String.class);
    }

    @Test
    void sendMessage_swallowRestClientException() {
        ReflectionTestUtils.setField(service, "botToken", "test-token");
        ReflectionTestUtils.setField(service, "chatId", "123456");

        doThrow(new RestClientException("boom"))
                .when(restTemplate)
                .getForObject(anyString(), eq(String.class));

        assertThatCode(() -> service.sendMessage("AAPL alert")).doesNotThrowAnyException();
        verify(restTemplate).getForObject(anyString(), eq(String.class));
    }
}