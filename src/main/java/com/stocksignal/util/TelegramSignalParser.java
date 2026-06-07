package com.stocksignal.util;

import com.stocksignal.dto.StockSignalRequest;
import com.stocksignal.entity.SignalType;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses free-form Telegram signal text into a {@link StockSignalRequest}.
 */
@Component
public class TelegramSignalParser {

    private static final Pattern LABELED_PATTERN = Pattern.compile(
            "종목\\s*[:=]\\s*([A-Z]{1,10})\\s*,?\\s*신호\\s*[:=]\\s*(BUY|SELL)\\s*,?\\s*가격\\s*[:=]\\s*([0-9]+(?:\\.[0-9]+)?)",
            Pattern.CASE_INSENSITIVE
    );

    private static final Pattern BRACKET_PATTERN = Pattern.compile(
            "\\[(BUY|SELL)\\]\\s*([A-Z]{1,10})\\s+([0-9]+(?:\\.[0-9]+)?)",
            Pattern.CASE_INSENSITIVE
    );

    public StockSignalRequest parse(String text) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Signal text must not be blank");
        }

        String normalizedText = text.trim();
        Matcher labeledMatcher = LABELED_PATTERN.matcher(normalizedText);
        if (labeledMatcher.find()) {
            return buildRequest(
                    labeledMatcher.group(1),
                    labeledMatcher.group(2),
                    labeledMatcher.group(3)
            );
        }

        Matcher bracketMatcher = BRACKET_PATTERN.matcher(normalizedText);
        if (bracketMatcher.find()) {
            return buildRequest(
                    bracketMatcher.group(2),
                    bracketMatcher.group(1),
                    bracketMatcher.group(3)
            );
        }

        throw new IllegalArgumentException("Unable to parse stock signal text: " + text);
    }

    private StockSignalRequest buildRequest(String ticker, String signalType, String price) {
        StockSignalRequest request = new StockSignalRequest();
        request.setTicker(ticker.toUpperCase(Locale.ROOT));
        request.setSignalType(SignalType.valueOf(signalType.toUpperCase(Locale.ROOT)));
        request.setPrice(Double.valueOf(price));
        return request;
    }
}