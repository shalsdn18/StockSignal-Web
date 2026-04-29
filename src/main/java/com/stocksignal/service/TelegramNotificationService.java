package com.stocksignal.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Service
public class TelegramNotificationService {

    private static final Logger log = LoggerFactory.getLogger(TelegramNotificationService.class);

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
        if (botToken.isBlank() || chatId.isBlank()) {
            log.info("[Telegram - not configured] {}", message);
            return;
        }
        try {
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8);
            String url = String.format(TELEGRAM_API_URL, botToken, chatId, encodedMessage);
            restTemplate.getForObject(url, String.class);
            log.info("Telegram notification sent: {}", message);
        } catch (RestClientException e) {
            log.error("Failed to send Telegram notification: {}", e.getMessage());
        }
    }
}
