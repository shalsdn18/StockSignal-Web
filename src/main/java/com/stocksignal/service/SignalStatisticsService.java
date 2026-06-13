package com.stocksignal.service;

import com.stocksignal.dto.SignalStatistics;
import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Core statistics algorithm layer for the dashboard.
 *
 * <p>Calculates the win rate by matching BUY and SELL signals per ticker
 * using a linear finite-state machine. The most recent unmatched BUY price
 * (LIFO) is used for each SELL. A completed trade is marked as a win when
 * the SELL price is strictly greater than the matching BUY price.</p>
 */
@Service
public class SignalStatisticsService {

    /**
     * Calculates signal statistics from the given list.
     *
     * <p>The input list is expected to be ordered by {@code createdAt} in ascending
     * order (oldest first), typically the result of
     * {@code repository.findAllByOrderByCreatedAtAsc()}.</p>
     *
     * @param signals input signals ordered by creation time ascending; may be empty or null
     * @return computed {@link SignalStatistics}
     */
    public SignalStatistics calculate(List<StockSignal> signals) {
        if (signals == null || signals.isEmpty()) {
            return new SignalStatistics(0, 0, 0, 0.0, 0, 0.0);
        }

        Map<String, List<StockSignal>> byTicker = signals.stream()
                .collect(Collectors.groupingBy(StockSignal::getTicker));

        int totalTrades = 0;
        int winningTrades = 0;
        int losingTrades = 0;
        int openPositions = 0;
        double cumulativeProfit = 0.0;

        for (List<StockSignal> tickerSignals : byTicker.values()) {
            Double currentBuyPrice = null;
            for (StockSignal signal : tickerSignals) {
                if (signal.getSignalType() == SignalType.BUY) {
                    currentBuyPrice = signal.getPrice();
                } else if (signal.getSignalType() == SignalType.SELL && currentBuyPrice != null) {
                    double pnl = signal.getPrice() - currentBuyPrice;
                    totalTrades++;
                    cumulativeProfit += pnl;

                    if (pnl > 0) {
                        winningTrades++;
                    } else {
                        losingTrades++;
                    }
                    currentBuyPrice = null;
                }
            }
            if (currentBuyPrice != null) {
                openPositions++;
            }
        }

        double winRate = totalTrades == 0
                ? 0.0
                : ((double) winningTrades / totalTrades) * 100.0;

        return new SignalStatistics(totalTrades, winningTrades, losingTrades, winRate, openPositions, cumulativeProfit);
    }
}
