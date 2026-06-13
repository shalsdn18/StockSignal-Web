package com.stocksignal.dto;

/**
 * Dashboard statistics for stock signal win-rate analysis.
 *
 * <p>A "trade" is a completed BUY -&gt; SELL pair for the same ticker.
 * A trade is considered a win when the SELL price is strictly higher
 * than the matching BUY price.</p>
 */
public class SignalStatistics {

    private int totalTrades;
    private int winCount;
    private int lossCount;
    private double winRate;
    private int openPositions;
    private double cumulativeProfit;

    public SignalStatistics() {
    }

    public SignalStatistics(int totalTrades,
                            int winCount,
                            int lossCount,
                            double winRate,
                            int openPositions,
                            double cumulativeProfit) {
        this.totalTrades = totalTrades;
        this.winCount = winCount;
        this.lossCount = lossCount;
        this.winRate = winRate;
        this.openPositions = openPositions;
        this.cumulativeProfit = cumulativeProfit;
    }

    /** Total number of completed BUY-&gt;SELL trades. */
    public int getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(int totalTrades) {
        this.totalTrades = totalTrades;
    }

    /** Number of winning trades (sell price &gt; buy price). */
    public int getWinCount() {
        return winCount;
    }

    public void setWinCount(int winCount) {
        this.winCount = winCount;
    }

    /** Number of losing or break-even trades (sell price &lt;= buy price). */
    public int getLossCount() {
        return lossCount;
    }

    public void setLossCount(int lossCount) {
        this.lossCount = lossCount;
    }

    /** Win rate as a percentage (0.0 - 100.0). */
    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    /** Number of BUY signals that have not yet been matched by a SELL signal. */
    public int getOpenPositions() {
        return openPositions;
    }

    public void setOpenPositions(int openPositions) {
        this.openPositions = openPositions;
    }

    /** Cumulative profit/loss across all completed BUY-SELL trades. */
    public double getCumulativeProfit() {
        return cumulativeProfit;
    }

    public void setCumulativeProfit(double cumulativeProfit) {
        this.cumulativeProfit = cumulativeProfit;
    }
}
