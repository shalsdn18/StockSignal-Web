package com.stocksignal.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

    private static final String TELEGRAM_API_URL =
            "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

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
        sendMessage(message, null, null);
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
        boolean useDynamicCredentials = dynamicToken != null && !dynamicToken.isBlank()
                && dynamicChatId != null && !dynamicChatId.isBlank();

        String effectiveToken = useDynamicCredentials ? dynamicToken : botToken;
        String effectiveChatId = useDynamicCredentials ? dynamicChatId : chatId;

        if (effectiveToken == null || effectiveToken.isBlank()
                || effectiveChatId == null || effectiveChatId.isBlank()) {
            log.info("[Telegram - not configured] {}", message);
            return;
        }
        try {
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
            String url = String.format(TELEGRAM_API_URL, effectiveToken, effectiveChatId, encodedMessage);
            restTemplate.getForObject(url, String.class);
            log.info("Telegram notification sent: {}", message);
        } catch (RestClientException e) {
            log.error("🚨 Telegram API sending failed", e);
        }
    }
}
