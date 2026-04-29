package com.stocksignal.controller;

import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import com.stocksignal.service.StockSignalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DashboardController.class)
class DashboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StockSignalService signalService;

    @Test
    void dashboard_rootPath_returnsOkAndDashboardView() throws Exception {
        StockSignal s = new StockSignal("AAPL", SignalType.BUY, 180.0, "test");
        s.setCreatedAt(LocalDateTime.now());
        when(signalService.getAllSignals()).thenReturn(List.of(s));
        when(signalService.getRecentSignals()).thenReturn(List.of(s));

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"))
                .andExpect(model().attributeExists("signals", "totalCount", "buyCount", "sellCount"));
    }

    @Test
    void dashboard_dashboardPath_returnsOkAndDashboardView() throws Exception {
        when(signalService.getAllSignals()).thenReturn(List.of());
        when(signalService.getRecentSignals()).thenReturn(List.of());

        mockMvc.perform(get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }
}
