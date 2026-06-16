package com.stocksignal.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stocksignal.dto.StockSignalRequest;
import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import com.stocksignal.service.StockSignalService;
import com.stocksignal.util.TelegramSignalParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class StockSignalApiControllerTest {

    private MockMvc mockMvc;
    private StockSignalService signalService;
    private TelegramSignalParser telegramSignalParser;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        signalService = mock(StockSignalService.class);
        telegramSignalParser = mock(TelegramSignalParser.class);
        objectMapper = new ObjectMapper();

        StockSignalApiController controller = new StockSignalApiController(signalService, telegramSignalParser);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    private StockSignal buildSignal(String ticker, SignalType type, double price) {
        StockSignal s = new StockSignal(ticker, type, price, "note");
        s.setId(1L);
        s.setCreatedAt(LocalDateTime.of(2026, 6, 16, 12, 0, 0));
        return s;
    }

    @Test
    void postSignal_returns201AndBody() throws Exception {
        StockSignalRequest request = new StockSignalRequest();
        request.setTicker("AAPL");
        request.setSignalType(SignalType.BUY);
        request.setPrice(182.50);

        StockSignal saved = buildSignal("AAPL", SignalType.BUY, 182.50);
        when(signalService.createSignal(any())).thenReturn(saved);

        mockMvc.perform(post("/api/signals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.ticker").value("AAPL"))
                .andExpect(jsonPath("$.signalType").value("BUY"));
    }

    @Test
    void webhook_returns201AndBody() throws Exception {
        StockSignalRequest request = new StockSignalRequest();
        request.setTicker("AAPL");
        request.setSignalType(SignalType.BUY);
        request.setPrice(182.50);

        StockSignal saved = buildSignal("AAPL", SignalType.BUY, 182.50);
        when(telegramSignalParser.parse("BUY AAPL 182.50")).thenReturn(request);
        when(signalService.createSignal(request)).thenReturn(saved);

        // 변경된 컨트롤러의 Map 반환 및 Enum->String 구조를 검증할 수 있도록 MockMvc 타겟 정정
        mockMvc.perform(post("/api/signals/webhook")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("BUY AAPL 182.50"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ticker").value("AAPL"))
                .andExpect(jsonPath("$.signalType").value("BUY"))
                .andExpect(jsonPath("$.price").value(182.50))
                .andExpect(jsonPath("$.createdAt").value("2026-06-16T12:00:00"));

        verify(signalService).createSignal(request);
    }

    @Test
    void webhook_invalidPayload_returns400() throws Exception {
        when(telegramSignalParser.parse("invalid payload"))
                .thenThrow(new IllegalArgumentException("Unable to parse stock signal text: invalid payload"));

        mockMvc.perform(post("/api/signals/webhook")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content("invalid payload"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void postSignal_invalidRequest_returns400() throws Exception {
        StockSignalRequest request = new StockSignalRequest();
        // missing required fields

        mockMvc.perform(post("/api/signals")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAllSignals_returnsListAndStatus200() throws Exception {
        when(signalService.getAllSignals())
                .thenReturn(List.of(buildSignal("TSLA", SignalType.SELL, 200.0)));

        mockMvc.perform(get("/api/signals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticker").value("TSLA"))
                .andExpect(jsonPath("$[0].signalType").value("SELL"));
    }

    @Test
    void getSignalById_found_returns200() throws Exception {
        StockSignal signal = buildSignal("GOOG", SignalType.BUY, 150.0);
        when(signalService.getSignalById(1L)).thenReturn(Optional.of(signal));

        mockMvc.perform(get("/api/signals/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ticker").value("GOOG"));
    }

    @Test
    void getSignalById_notFound_returns404() throws Exception {
        when(signalService.getSignalById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/signals/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByTicker_returnsFilteredList() throws Exception {
        when(signalService.getSignalsByTicker("AAPL"))
                .thenReturn(List.of(buildSignal("AAPL", SignalType.BUY, 180.0)));

        mockMvc.perform(get("/api/signals/ticker/AAPL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].ticker").value("AAPL"));
    }

    @Test
    void getByType_returnsFilteredList() throws Exception {
        when(signalService.getSignalsByType(SignalType.BUY))
                .thenReturn(List.of(buildSignal("AMZN", SignalType.BUY, 175.0)));

        mockMvc.perform(get("/api/signals/type/BUY"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].signalType").value("BUY"));
    }
}