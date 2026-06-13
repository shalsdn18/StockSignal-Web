package com.stocksignal.service;

import com.stocksignal.dto.SignalStatistics;
import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;

class SignalStatisticsServiceTest {

    private final SignalStatisticsService service = new SignalStatisticsService();

    @Test
    void calculate_returnsZeroStatsForEmptyList() {
        SignalStatistics stats = service.calculate(List.of());

        assertThat(stats.getTotalTrades()).isZero();
        assertThat(stats.getWinCount()).isZero();
        assertThat(stats.getLossCount()).isZero();
        assertThat(stats.getWinRate()).isZero();
        assertThat(stats.getOpenPositions()).isZero();
        assertThat(stats.getCumulativeProfit()).isZero();
    }

    @Test
    void calculate_returnsZeroStatsForNullList() {
        SignalStatistics stats = service.calculate(null);

        assertThat(stats.getTotalTrades()).isZero();
        assertThat(stats.getWinRate()).isZero();
        assertThat(stats.getCumulativeProfit()).isZero();
    }

    @Test
    void calculate_countsWinWhenSellPriceIsHigherThanBuyPrice() {
        StockSignal buy = signal("AAPL", SignalType.BUY, 100.0, minutesAgo(1));
        StockSignal sell = signal("AAPL", SignalType.SELL, 110.0, minutesAgo(0));

        SignalStatistics stats = service.calculate(List.of(buy, sell));

        assertThat(stats.getTotalTrades()).isEqualTo(1);
        assertThat(stats.getWinCount()).isEqualTo(1);
        assertThat(stats.getLossCount()).isZero();
        assertThat(stats.getWinRate()).isCloseTo(100.0, within(0.01));
        assertThat(stats.getOpenPositions()).isZero();
        assertThat(stats.getCumulativeProfit()).isCloseTo(10.0, within(0.01));
    }

    @Test
    void calculate_countsLossWhenSellPriceIsLowerThanBuyPrice() {
        StockSignal buy = signal("AAPL", SignalType.BUY, 100.0, minutesAgo(1));
        StockSignal sell = signal("AAPL", SignalType.SELL, 90.0, minutesAgo(0));

        SignalStatistics stats = service.calculate(List.of(buy, sell));

        assertThat(stats.getTotalTrades()).isEqualTo(1);
        assertThat(stats.getWinCount()).isZero();
        assertThat(stats.getLossCount()).isEqualTo(1);
        assertThat(stats.getWinRate()).isZero();
        assertThat(stats.getOpenPositions()).isZero();
        assertThat(stats.getCumulativeProfit()).isCloseTo(-10.0, within(0.01));
    }

    @Test
    void calculate_countsLossOnBreakEvenTrade() {
        StockSignal buy = signal("AAPL", SignalType.BUY, 100.0, minutesAgo(1));
        StockSignal sell = signal("AAPL", SignalType.SELL, 100.0, minutesAgo(0));

        SignalStatistics stats = service.calculate(List.of(buy, sell));

        assertThat(stats.getWinCount()).isZero();
        assertThat(stats.getLossCount()).isEqualTo(1);
        assertThat(stats.getWinRate()).isZero();
        assertThat(stats.getCumulativeProfit()).isZero();
    }

    @Test
    void calculate_appliesLifoStateMachineForConsecutiveBuys() {
        StockSignal buy1 = signal("AAPL", SignalType.BUY, 100.0, minutesAgo(2));
        StockSignal buy2 = signal("AAPL", SignalType.BUY, 90.0, minutesAgo(1));
        StockSignal sell = signal("AAPL", SignalType.SELL, 95.0, minutesAgo(0));

        SignalStatistics stats = service.calculate(List.of(buy1, buy2, sell));

        // LIFO FSM: sell@95 closes the latest buy2@90 -> win; buy1 is overwritten
        assertThat(stats.getTotalTrades()).isEqualTo(1);
        assertThat(stats.getWinCount()).isEqualTo(1);
        assertThat(stats.getLossCount()).isZero();
        assertThat(stats.getOpenPositions()).isZero();
        assertThat(stats.getCumulativeProfit()).isCloseTo(5.0, within(0.01));
    }

    @Test
    void calculate_ignoresOrphanSellWithoutPriorBuy() {
        StockSignal sell = signal("AAPL", SignalType.SELL, 110.0, minutesAgo(1));
        StockSignal buy = signal("AAPL", SignalType.BUY, 100.0, minutesAgo(0));

        SignalStatistics stats = service.calculate(List.of(sell, buy));

        // The SELL happened before the BUY, so it has no matching entry.
        // The BUY remains an open position.
        assertThat(stats.getTotalTrades()).isZero();
        assertThat(stats.getWinCount()).isZero();
        assertThat(stats.getOpenPositions()).isEqualTo(1);
        assertThat(stats.getCumulativeProfit()).isZero();
    }

    @Test
    void calculate_groupsByTicker() {
        StockSignal buy = signal("AAPL", SignalType.BUY, 100.0, minutesAgo(1));
        StockSignal sell = signal("AAPL", SignalType.SELL, 110.0, minutesAgo(0));

        SignalStatistics stats = service.calculate(List.of(buy, sell));

        assertThat(stats.getTotalTrades()).isEqualTo(1);
        assertThat(stats.getWinCount()).isEqualTo(1);
    }

    @Test
    void calculate_computesWinRateAcrossMultipleTickers() {
        StockSignal aaplBuy = signal("AAPL", SignalType.BUY, 100.0, minutesAgo(3));
        StockSignal aaplSell = signal("AAPL", SignalType.SELL, 110.0, minutesAgo(2));
        StockSignal tslaBuy = signal("TSLA", SignalType.BUY, 200.0, minutesAgo(1));
        StockSignal tslaSell = signal("TSLA", SignalType.SELL, 190.0, minutesAgo(0));

        SignalStatistics stats = service.calculate(List.of(aaplBuy, aaplSell, tslaBuy, tslaSell));

        assertThat(stats.getTotalTrades()).isEqualTo(2);
        assertThat(stats.getWinCount()).isEqualTo(1);
        assertThat(stats.getLossCount()).isEqualTo(1);
        assertThat(stats.getWinRate()).isCloseTo(50.0, within(0.01));
        assertThat(stats.getCumulativeProfit()).isCloseTo(0.0, within(0.01));
    }

    @Test
    void calculate_tracksOpenBuyWithoutMatchingSell() {
        StockSignal buy = signal("AAPL", SignalType.BUY, 100.0, minutesAgo(0));

        SignalStatistics stats = service.calculate(List.of(buy));

        assertThat(stats.getTotalTrades()).isZero();
        assertThat(stats.getOpenPositions()).isEqualTo(1);
        assertThat(stats.getCumulativeProfit()).isZero();
    }

    @Test
    void calculate_sumsCumulativeProfitAcrossMultipleTrades() {
        StockSignal aaplBuy = signal("AAPL", SignalType.BUY, 100.0, minutesAgo(4));
        StockSignal aaplSell = signal("AAPL", SignalType.SELL, 115.0, minutesAgo(3));
        StockSignal tslaBuy = signal("TSLA", SignalType.BUY, 200.0, minutesAgo(2));
        StockSignal tslaSell = signal("TSLA", SignalType.SELL, 180.0, minutesAgo(1));

        SignalStatistics stats = service.calculate(List.of(aaplBuy, aaplSell, tslaBuy, tslaSell));

        assertThat(stats.getTotalTrades()).isEqualTo(2);
        assertThat(stats.getCumulativeProfit()).isCloseTo(-5.0, within(0.01)); // +15 - 20
    }

    private StockSignal signal(String ticker, SignalType type, double price, LocalDateTime createdAt) {
        StockSignal signal = new StockSignal(ticker, type, price, null);
        signal.setCreatedAt(createdAt);
        return signal;
    }

    private LocalDateTime minutesAgo(int minutes) {
        return LocalDateTime.now().minusMinutes(minutes);
    }
}
