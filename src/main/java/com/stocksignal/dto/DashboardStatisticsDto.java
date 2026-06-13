package com.stocksignal.dto;

/**
 * Aggregate DTO for all dashboard-level statistics.
 *
 * <p>Combines the filtered signal counts displayed in the summary cards
 * with the archived win-rate analysis fields in a flat structure.</p>
 */
public class DashboardStatisticsDto {

    private long totalCount;
    private long buyCount;
    private long sellCount;
    private long totalTrades;
    private long winningTrades;
    private long losingTrades;
    private double winRate;
    private long openPositions;
    private double cumulativeProfit;

    public DashboardStatisticsDto() {
    }

    public DashboardStatisticsDto(long totalTrades,
                                  long winningTrades,
                                  double winRate,
                                  double cumulativeProfit) {
        this.totalTrades = totalTrades;
        this.winningTrades = winningTrades;
        this.winRate = winRate;
        this.cumulativeProfit = cumulativeProfit;
    }

    public DashboardStatisticsDto(long totalTrades,
                                  long winningTrades,
                                  long losingTrades,
                                  double winRate,
                                  double cumulativeProfit) {
        this.totalTrades = totalTrades;
        this.winningTrades = winningTrades;
        this.losingTrades = losingTrades;
        this.winRate = winRate;
        this.cumulativeProfit = cumulativeProfit;
    }

    public DashboardStatisticsDto(long totalCount,
                                  long buyCount,
                                  long sellCount,
                                  long totalTrades,
                                  long winningTrades,
                                  long losingTrades,
                                  double winRate,
                                  long openPositions,
                                  double cumulativeProfit) {
        this.totalCount = totalCount;
        this.buyCount = buyCount;
        this.sellCount = sellCount;
        this.totalTrades = totalTrades;
        this.winningTrades = winningTrades;
        this.losingTrades = losingTrades;
        this.winRate = winRate;
        this.openPositions = openPositions;
        this.cumulativeProfit = cumulativeProfit;
    }

    public DashboardStatisticsDto(long totalCount,
                                  long buyCount,
                                  long sellCount,
                                  SignalStatistics signalStatistics) {
        this.totalCount = totalCount;
        this.buyCount = buyCount;
        this.sellCount = sellCount;
        this.totalTrades = signalStatistics.getTotalTrades();
        this.winningTrades = signalStatistics.getWinCount();
        this.losingTrades = signalStatistics.getLossCount();
        this.winRate = signalStatistics.getWinRate();
        this.openPositions = signalStatistics.getOpenPositions();
        this.cumulativeProfit = signalStatistics.getCumulativeProfit();
    }

    /** Total number of signals currently displayed after filters. */
    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    /** Number of BUY signals currently displayed after filters. */
    public long getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(long buyCount) {
        this.buyCount = buyCount;
    }

    /** Number of SELL signals currently displayed after filters. */
    public long getSellCount() {
        return sellCount;
    }

    public void setSellCount(long sellCount) {
        this.sellCount = sellCount;
    }

    /** Total number of completed BUY-SELL trades across the entire archive. */
    public long getTotalTrades() {
        return totalTrades;
    }

    public void setTotalTrades(long totalTrades) {
        this.totalTrades = totalTrades;
    }

    /** Number of winning trades (sell price &gt; buy price). */
    public long getWinningTrades() {
        return winningTrades;
    }

    public void setWinningTrades(long winningTrades) {
        this.winningTrades = winningTrades;
    }

    /** Number of losing or break-even trades (sell price &lt;= buy price). */
    public long getLosingTrades() {
        return losingTrades;
    }

    public void setLosingTrades(long losingTrades) {
        this.losingTrades = losingTrades;
    }

    /** Win rate as a percentage (0.0 - 100.0). */
    public double getWinRate() {
        return winRate;
    }

    public void setWinRate(double winRate) {
        this.winRate = winRate;
    }

    /** Number of BUY signals without a matching SELL signal. */
    public long getOpenPositions() {
        return openPositions;
    }

    public void setOpenPositions(long openPositions) {
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
