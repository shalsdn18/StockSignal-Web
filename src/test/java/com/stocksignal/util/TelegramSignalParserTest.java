package com.stocksignal.util;

import com.stocksignal.dto.StockSignalRequest;
import com.stocksignal.entity.SignalType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class TelegramSignalParserTest {

    private final TelegramSignalParser parser = new TelegramSignalParser();

    @Test
    void parse_labeledText_extractsFields() {
        StockSignalRequest request = parser.parse("종목: AAPL, 신호: BUY, 가격: 182.50");

        assertThat(request.getTicker()).isEqualTo("AAPL");
        assertThat(request.getSignalType()).isEqualTo(SignalType.BUY);
        assertThat(request.getPrice()).isEqualTo(182.50);
    }

    @Test
    void parse_bracketText_extractsFields() {
        StockSignalRequest request = parser.parse("[SELL] TSLA 198.40");

        assertThat(request.getTicker()).isEqualTo("TSLA");
        assertThat(request.getSignalType()).isEqualTo(SignalType.SELL);
        assertThat(request.getPrice()).isEqualTo(198.40);
    }

    @Test
    void parse_blankText_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> parser.parse("   "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must not be blank");
    }

    @Test
    void parse_invalidText_throwsIllegalArgumentException() {
        assertThatThrownBy(() -> parser.parse("hello world"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Unable to parse stock signal text");
    }
}