package com.stocksignal.service;

import com.stocksignal.entity.SystemConfig;
import com.stocksignal.repository.SystemConfigRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TelegramNotificationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private SystemConfigRepository configRepository;

    private TelegramNotificationService service;

    private SystemConfig mockConfig;

    @BeforeEach
    void setUp() {
        service = new TelegramNotificationService(restTemplate, configRepository);
        mockConfig = new SystemConfig();
        // 동적 설정 조회 기능에 대한 무결성 모킹 스텁 정의
        when(configRepository.findById(1L)).thenReturn(Optional.of(mockConfig));
    }

    @Test
    void sendMessage_doesNotCallApiWhenConfigurationIsMissing() {
        mockConfig.setTelegramBotToken("");
        mockConfig.setTelegramChatId("123456");

        service.sendMessage("Signal ready");

        verifyNoInteractions(restTemplate);
    }

    @Test
    void sendMessage_callsTelegramApiWhenConfigured() {
        mockConfig.setTelegramBotToken("test-token");
        mockConfig.setTelegramChatId("123456");

        service.sendMessage("Buy AAPL at 182.50");

        String expectedUrl = "https://api.telegram.org/bot{token}/sendMessage?chat_id={chatId}&text={text}";
        verify(restTemplate).getForObject(expectedUrl, String.class, "test-token", "123456", "Buy AAPL at 182.50");
    }

    @Test
    void sendMessage_swallowRestClientException() {
        mockConfig.setTelegramBotToken("test-token");
        mockConfig.setTelegramChatId("123456");

        doThrow(new RestClientException("boom"))
                .when(restTemplate)
                .getForObject(anyString(), eq(String.class), anyString(), anyString(), anyString());

        assertThatCode(() -> service.sendMessage("AAPL alert")).doesNotThrowAnyException();
        verify(restTemplate).getForObject(anyString(), eq(String.class), anyString(), anyString(), anyString());
    }
}
