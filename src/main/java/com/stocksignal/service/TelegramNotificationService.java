package com.stocksignal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Service for sending notifications via the Telegram Bot API.
 *
 * <p>Set {@code telegram.bot.token} and {@code telegram.bot.chat-id} in
 * {@code application.properties} (or as environment variables) to enable live
 * delivery.  When either value is left blank the service logs the message
 * instead of calling the API so the rest of the application continues to work
 * without a configured bot.
 */
@Slf4j
@Service
public class TelegramNotificationService {

    private final RestTemplate restTemplate;

    @Value("${telegram.bot.token:}")
    private String botToken;

    @Value("${telegram.bot.chat-id:}")
    private String chatId;

    public TelegramNotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Sends {@code message} to the configured Telegram chat.
     * If the bot token or chat ID is not configured, the message is only logged.
     *
     * @param message the text to send
     */
    public void sendMessage(String message) {
        sendWithCredentials(message, botToken, chatId);
    }

    /**
     * Sends {@code message} using dynamic Telegram credentials when provided.
     * Falls back to the configured properties if dynamic credentials are blank.
     *
     * @param message the text to send
     * @param dynamicToken the runtime bot token, optionally supplied from the database
     * @param dynamicChatId the runtime chat ID, optionally supplied from the database
     */
    public void sendMessage(String message, String dynamicToken, String dynamicChatId) {
        if (dynamicToken == null || dynamicToken.isBlank()
                || dynamicChatId == null || dynamicChatId.isBlank()) {
            sendMessage(message);
            return;
        }

        sendWithCredentials(message, dynamicToken, dynamicChatId);
    }

    private void sendWithCredentials(String message, String effectiveToken, String effectiveChatId) {
        if (effectiveToken == null || effectiveToken.isBlank()
                || effectiveChatId == null || effectiveChatId.isBlank()) {
            log.info("[Telegram - not configured] {}", message);
            return;
        }

        try {
            // 중복 인코딩(Double Encoding) 방지를 위해 RestTemplate 가이드라인 가변 인자 구조 적용
            String url = "https://api.telegram.org/bot{token}/sendMessage?chat_id={chatId}&text={text}";
            
            // RestTemplate이 각 중괄호 위치에 값을 순서대로 안전하게 자동 인코딩 매핑해 줍니다.
            restTemplate.getForObject(url, String.class, effectiveToken, effectiveChatId, message);
            log.info("Telegram notification sent: {}", message);
        } catch (RestClientException e) {
            log.error("🚨 Telegram API sending failed", e);
        }
    }
}