package com.stocksignal.controller;

import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import com.stocksignal.service.StockSignalService;
import org.junit.jupiter.api.Test;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
class DashboardControllerTest {

    private final StockSignalService signalService = org.mockito.Mockito.mock(StockSignalService.class);
    private final DashboardController controller = new DashboardController(signalService);

    @Test
    void dashboard_populatesSignalsAndStats() {
        StockSignal s = new StockSignal("AAPL", SignalType.BUY, 180.0, "test");
        s.setCreatedAt(LocalDateTime.now());
        when(signalService.searchSignalsByDynamicFilters(null, null, null, null)).thenReturn(List.of(s));

        Model model = new ConcurrentModel();

        String viewName = controller.dashboard(null, null, null, null, model);

        org.assertj.core.api.Assertions.assertThat(viewName).isEqualTo("dashboard");
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("signals")).isEqualTo(List.of(s));
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("totalCount")).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("buyCount")).isEqualTo(1L);
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("sellCount")).isEqualTo(0L);
    }

    @Test
    void dashboard_usesTickerFilterWhenTickerProvided() {
        StockSignal s = new StockSignal("AAPL", SignalType.BUY, 180.0, "test");
        s.setCreatedAt(LocalDateTime.now());
        when(signalService.searchSignalsByDynamicFilters("AAP", null, null, null)).thenReturn(List.of(s));

        Model model = new ConcurrentModel();

        String viewName = controller.dashboard("AAP", null, null, null, model);

        org.assertj.core.api.Assertions.assertThat(viewName).isEqualTo("dashboard");
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("signals")).isEqualTo(List.of(s));
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("totalCount")).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("buyCount")).isEqualTo(1L);
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("sellCount")).isEqualTo(0L);
    }

    @Test
    void dashboard_usesDateRangeFilterWhenDatesProvided() {
        StockSignal s = new StockSignal("MSFT", SignalType.SELL, 320.0, "test");
        s.setCreatedAt(LocalDateTime.now());
        java.time.LocalDate today = java.time.LocalDate.now();
        when(signalService.searchSignalsByDynamicFilters(null, today, today, null)).thenReturn(List.of(s));

        Model model = new ConcurrentModel();

        String viewName = controller.dashboard(null, today, today, null, model);

        org.assertj.core.api.Assertions.assertThat(viewName).isEqualTo("dashboard");
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("signals")).isEqualTo(List.of(s));
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("totalCount")).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("buyCount")).isEqualTo(0L);
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("sellCount")).isEqualTo(1L);
    }

    @Test
    void dashboard_usesSignalTypeFilterWhenSignalTypeProvided() {
        StockSignal s = new StockSignal("AAPL", SignalType.BUY, 180.0, "test");
        s.setCreatedAt(LocalDateTime.now());
        when(signalService.searchSignalsByDynamicFilters(null, null, null, SignalType.BUY)).thenReturn(List.of(s));

        Model model = new ConcurrentModel();

        String viewName = controller.dashboard(null, null, null, SignalType.BUY, model);

        org.assertj.core.api.Assertions.assertThat(viewName).isEqualTo("dashboard");
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("signals")).isEqualTo(List.of(s));
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("totalCount")).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("buyCount")).isEqualTo(1L);
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("sellCount")).isEqualTo(0L);
    }

    @Test
    void dashboard_usesAllFiltersWhenAllProvided() {
        StockSignal s = new StockSignal("AAPL", SignalType.BUY, 180.0, "test");
        s.setCreatedAt(LocalDateTime.now());
        java.time.LocalDate today = java.time.LocalDate.now();
        when(signalService.searchSignalsByDynamicFilters("AAP", today, today, SignalType.BUY)).thenReturn(List.of(s));

        Model model = new ConcurrentModel();

        String viewName = controller.dashboard("AAP", today, today, SignalType.BUY, model);

        org.assertj.core.api.Assertions.assertThat(viewName).isEqualTo("dashboard");
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("signals")).isEqualTo(List.of(s));
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("totalCount")).isEqualTo(1);
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("buyCount")).isEqualTo(1L);
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("sellCount")).isEqualTo(0L);
    }

    @Test
    void dashboard_handlesEmptySignals() {
        when(signalService.searchSignalsByDynamicFilters(null, null, null, null)).thenReturn(List.of());

        Model model = new ConcurrentModel();

        String viewName = controller.dashboard(null, null, null, null, model);

        org.assertj.core.api.Assertions.assertThat(viewName).isEqualTo("dashboard");
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("signals")).isEqualTo(List.of());
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("totalCount")).isEqualTo(0);
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("buyCount")).isEqualTo(0L);
        org.assertj.core.api.Assertions.assertThat(model.getAttribute("sellCount")).isEqualTo(0L);
    }
}
