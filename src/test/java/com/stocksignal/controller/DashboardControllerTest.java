package com.stocksignal.controller;

import com.stocksignal.dto.DashboardStatisticsDto;
import com.stocksignal.dto.SignalStatistics;
import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import com.stocksignal.service.StockSignalService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DashboardControllerTest {

    private final StockSignalService signalService = mock(StockSignalService.class);
    private final DashboardController controller = new DashboardController(signalService);

    @Test
    void dashboard_populatesSignalsAndStats() {
        StockSignal s = new StockSignal("AAPL", SignalType.BUY, 180.0, "test");
        s.setCreatedAt(LocalDateTime.now());
        SignalStatistics signalStats = new SignalStatistics(0, 0, 0, 0.0, 1, 0.0);
        List<StockSignal> allSignals = List.of(s);
        LocalDateTime lastSignalReceivedAt = LocalDateTime.now();

        when(signalService.searchSignalsByDynamicFilters(null, null, null, null)).thenReturn(allSignals);
        when(signalService.calculateOverallStatistics()).thenReturn(
                new DashboardStatisticsDto(1L, 1L, 0L, signalStats));
        when(signalService.getLastSignalReceivedAt()).thenReturn(lastSignalReceivedAt);

        Model model = new ConcurrentModel();

        String viewName = controller.dashboard(null, null, null, null, model);

        assertThat(viewName).isEqualTo("dashboard");
        assertThat(model.getAttribute("signals")).isEqualTo(allSignals);
        assertThat(model.getAttribute("lastSignalReceivedAt")).isEqualTo(lastSignalReceivedAt);

        DashboardStatisticsDto dto = (DashboardStatisticsDto) model.getAttribute("dashboardStatistics");
        assertThat(dto).isNotNull();
        assertThat(dto.getTotalCount()).isEqualTo(1L);
        assertThat(dto.getBuyCount()).isEqualTo(1L);
        assertThat(dto.getSellCount()).isEqualTo(0L);
        assertThat(dto.getTotalTrades()).isEqualTo(0L);
        assertThat(dto.getWinningTrades()).isEqualTo(0L);
        assertThat(dto.getLosingTrades()).isEqualTo(0L);
        assertThat(dto.getWinRate()).isEqualTo(0.0);
        assertThat(dto.getOpenPositions()).isEqualTo(1L);
        assertThat(dto.getCumulativeProfit()).isEqualTo(0.0);
    }

    @Test
    void dashboard_usesTickerFilterWhenTickerProvided() {
        StockSignal s = new StockSignal("AAPL", SignalType.BUY, 180.0, "test");
        s.setCreatedAt(LocalDateTime.now());
        SignalStatistics signalStats = new SignalStatistics(1, 1, 0, 100.0, 0, 10.0);
        List<StockSignal> allSignals = List.of(s);
        LocalDateTime lastSignalReceivedAt = LocalDateTime.now();

        when(signalService.searchSignalsByDynamicFilters("AAP", null, null, null)).thenReturn(allSignals);
        when(signalService.calculateOverallStatistics()).thenReturn(
                new DashboardStatisticsDto(1L, 1L, 0L, signalStats));
        when(signalService.getLastSignalReceivedAt()).thenReturn(lastSignalReceivedAt);

        Model model = new ConcurrentModel();

        String viewName = controller.dashboard("AAP", null, null, null, model);

        assertThat(viewName).isEqualTo("dashboard");
        DashboardStatisticsDto dto = (DashboardStatisticsDto) model.getAttribute("dashboardStatistics");
        assertThat(dto.getTotalCount()).isEqualTo(1L);
        assertThat(dto.getBuyCount()).isEqualTo(1L);
        assertThat(dto.getSellCount()).isEqualTo(0L);
        assertThat(dto.getTotalTrades()).isEqualTo(1L);
        assertThat(dto.getWinningTrades()).isEqualTo(1L);
        assertThat(dto.getWinRate()).isEqualTo(100.0);
        assertThat(dto.getCumulativeProfit()).isEqualTo(10.0);
    }

    @Test
    void dashboard_usesDateRangeFilterWhenDatesProvided() {
        StockSignal s = new StockSignal("MSFT", SignalType.SELL, 320.0, "test");
        s.setCreatedAt(LocalDateTime.now());
        java.time.LocalDate today = java.time.LocalDate.now();
        SignalStatistics signalStats = new SignalStatistics(0, 0, 0, 0.0, 0, 0.0);
        List<StockSignal> allSignals = List.of(s);
        LocalDateTime lastSignalReceivedAt = LocalDateTime.now();

        when(signalService.searchSignalsByDynamicFilters(null, today, today, null)).thenReturn(allSignals);
        when(signalService.calculateOverallStatistics()).thenReturn(
                new DashboardStatisticsDto(1L, 0L, 1L, signalStats));
        when(signalService.getLastSignalReceivedAt()).thenReturn(lastSignalReceivedAt);

        Model model = new ConcurrentModel();

        String viewName = controller.dashboard(null, today, today, null, model);

        assertThat(viewName).isEqualTo("dashboard");
        DashboardStatisticsDto dto = (DashboardStatisticsDto) model.getAttribute("dashboardStatistics");
        assertThat(dto.getTotalCount()).isEqualTo(1L);
        assertThat(dto.getBuyCount()).isEqualTo(0L);
        assertThat(dto.getSellCount()).isEqualTo(1L);
        assertThat(dto.getTotalTrades()).isEqualTo(0L);
        assertThat(dto.getCumulativeProfit()).isEqualTo(0.0);
    }

    @Test
    void dashboard_usesSignalTypeFilterWhenSignalTypeProvided() {
        StockSignal s = new StockSignal("AAPL", SignalType.BUY, 180.0, "test");
        s.setCreatedAt(LocalDateTime.now());
        SignalStatistics signalStats = new SignalStatistics(0, 0, 0, 0.0, 1, 0.0);
        List<StockSignal> allSignals = List.of(s);
        LocalDateTime lastSignalReceivedAt = LocalDateTime.now();

        when(signalService.searchSignalsByDynamicFilters(null, null, null, SignalType.BUY)).thenReturn(allSignals);
        when(signalService.calculateOverallStatistics()).thenReturn(
                new DashboardStatisticsDto(1L, 1L, 0L, signalStats));
        when(signalService.getLastSignalReceivedAt()).thenReturn(lastSignalReceivedAt);

        Model model = new ConcurrentModel();

        String viewName = controller.dashboard(null, null, null, SignalType.BUY, model);

        assertThat(viewName).isEqualTo("dashboard");
        DashboardStatisticsDto dto = (DashboardStatisticsDto) model.getAttribute("dashboardStatistics");
        assertThat(dto.getTotalCount()).isEqualTo(1L);
        assertThat(dto.getBuyCount()).isEqualTo(1L);
        assertThat(dto.getSellCount()).isEqualTo(0L);
        assertThat(dto.getOpenPositions()).isEqualTo(1L);
        assertThat(dto.getCumulativeProfit()).isEqualTo(0.0);
    }

    @Test
    void dashboard_usesAllFiltersWhenAllProvided() {
        StockSignal s = new StockSignal("AAPL", SignalType.BUY, 180.0, "test");
        s.setCreatedAt(LocalDateTime.now());
        java.time.LocalDate today = java.time.LocalDate.now();
        SignalStatistics signalStats = new SignalStatistics(0, 0, 0, 0.0, 1, 0.0);
        List<StockSignal> allSignals = List.of(s);
        LocalDateTime lastSignalReceivedAt = LocalDateTime.now();

        when(signalService.searchSignalsByDynamicFilters("AAP", today, today, SignalType.BUY)).thenReturn(allSignals);
        when(signalService.calculateOverallStatistics()).thenReturn(
                new DashboardStatisticsDto(1L, 1L, 0L, signalStats));
        when(signalService.getLastSignalReceivedAt()).thenReturn(lastSignalReceivedAt);

        Model model = new ConcurrentModel();

        String viewName = controller.dashboard("AAP", today, today, SignalType.BUY, model);

        assertThat(viewName).isEqualTo("dashboard");
        DashboardStatisticsDto dto = (DashboardStatisticsDto) model.getAttribute("dashboardStatistics");
        assertThat(dto.getTotalCount()).isEqualTo(1L);
        assertThat(dto.getBuyCount()).isEqualTo(1L);
        assertThat(dto.getSellCount()).isEqualTo(0L);
        assertThat(dto.getOpenPositions()).isEqualTo(1L);
        assertThat(dto.getCumulativeProfit()).isEqualTo(0.0);
    }

    @Test
    void dashboard_handlesEmptySignals() {
        SignalStatistics signalStats = new SignalStatistics(0, 0, 0, 0.0, 0, 0.0);
        LocalDateTime lastSignalReceivedAt = LocalDateTime.now();

        when(signalService.searchSignalsByDynamicFilters(null, null, null, null)).thenReturn(List.of());
        when(signalService.calculateOverallStatistics()).thenReturn(
                new DashboardStatisticsDto(0L, 0L, 0L, signalStats));
        when(signalService.getLastSignalReceivedAt()).thenReturn(lastSignalReceivedAt);

        Model model = new ConcurrentModel();

        String viewName = controller.dashboard(null, null, null, null, model);

        assertThat(viewName).isEqualTo("dashboard");
        DashboardStatisticsDto dto = (DashboardStatisticsDto) model.getAttribute("dashboardStatistics");
        assertThat(dto).isNotNull();
        assertThat(model.getAttribute("lastSignalReceivedAt")).isEqualTo(lastSignalReceivedAt);
        assertThat(dto.getTotalCount()).isEqualTo(0L);
        assertThat(dto.getBuyCount()).isEqualTo(0L);
        assertThat(dto.getSellCount()).isEqualTo(0L);
        assertThat(dto.getTotalTrades()).isEqualTo(0L);
        assertThat(dto.getWinningTrades()).isEqualTo(0L);
        assertThat(dto.getLosingTrades()).isEqualTo(0L);
        assertThat(dto.getWinRate()).isEqualTo(0.0);
        assertThat(dto.getOpenPositions()).isEqualTo(0L);
        assertThat(dto.getCumulativeProfit()).isEqualTo(0.0);
    }
}
