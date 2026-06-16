package com.stocksignal.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

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

        // RestTemplate 플레이스홀더 패턴 매칭 설정 동기화 완료
        String expectedUrl = "https://api.telegram.org/bot{token}/sendMessage?chat_id={chatId}&text={text}";
        verify(restTemplate).getForObject(expectedUrl, String.class, "test-token", "123456", "Buy AAPL at 182.50");
    }

    @Test
    void sendMessage_swallowRestClientException() {
        ReflectionTestUtils.setField(service, "botToken", "test-token");
        ReflectionTestUtils.setField(service, "chatId", "123456");

        // 가변 인자 3개를 온전히 받아 예외를 터뜨리도록 정교화
        doThrow(new RestClientException("boom"))
                .when(restTemplate)
                .getForObject(anyString(), eq(String.class), anyString(), anyString(), anyString());

        assertThatCode(() -> service.sendMessage("AAPL alert")).doesNotThrowAnyException();
        verify(restTemplate).getForObject(anyString(), eq(String.class), anyString(), anyString(), anyString());
    }
}