package com.stocksignal.service;

import com.stocksignal.dto.StockSignalRequest;
import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import com.stocksignal.repository.StockSignalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockSignalServiceTest {

    @Mock
    private StockSignalRepository repository;

    @Mock
    private TelegramNotificationService telegramService;

    @InjectMocks
    private StockSignalService service;

    private StockSignal sampleSignal;

    @BeforeEach
    void setUp() {
        sampleSignal = new StockSignal("AAPL", SignalType.BUY, 182.50, "Test signal");
        sampleSignal.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void createSignal_savesEntityAndSendsTelegram() {
        StockSignalRequest request = new StockSignalRequest();
        request.setTicker("aapl");
        request.setSignalType(SignalType.BUY);
        request.setPrice(182.50);
        request.setMessage("Test signal");

        when(repository.save(any(StockSignal.class))).thenReturn(sampleSignal);

        StockSignal result = service.createSignal(request);

        assertThat(result).isNotNull();
        assertThat(result.getTicker()).isEqualTo("AAPL");
        assertThat(result.getSignalType()).isEqualTo(SignalType.BUY);
        verify(repository).save(any(StockSignal.class));
        verify(telegramService).sendMessage(anyString());
    }

    @Test
    void createSignal_tickerIsUppercased() {
        StockSignalRequest request = new StockSignalRequest();
        request.setTicker("tsla");
        request.setSignalType(SignalType.SELL);
        request.setPrice(200.0);

        StockSignal expected = new StockSignal("TSLA", SignalType.SELL, 200.0, null);
        expected.setCreatedAt(LocalDateTime.now());
        when(repository.save(any(StockSignal.class))).thenAnswer(inv -> {
            StockSignal s = inv.getArgument(0);
            assertThat(s.getTicker()).isEqualTo("TSLA");
            return s;
        });

        service.createSignal(request);
    }

    @Test
    void getAllSignals_returnsListSortedNewestFirst() {
        StockSignal older = new StockSignal("GOOG", SignalType.BUY, 100.0, null);
        older.setCreatedAt(LocalDateTime.now().minusDays(1));
        StockSignal newer = new StockSignal("MSFT", SignalType.SELL, 200.0, null);
        newer.setCreatedAt(LocalDateTime.now());

        when(repository.findAll()).thenReturn(List.of(older, newer));

        List<StockSignal> result = service.getAllSignals();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTicker()).isEqualTo("MSFT");
    }

    @Test
    void getSignalById_returnsCorrectSignal() {
        when(repository.findById(1L)).thenReturn(Optional.of(sampleSignal));

        Optional<StockSignal> result = service.getSignalById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTicker()).isEqualTo("AAPL");
    }

    @Test
    void getSignalById_returnsEmptyWhenNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<StockSignal> result = service.getSignalById(99L);

        assertThat(result).isEmpty();
    }

    @Test
    void getSignalsByTicker_delegatesToRepository() {
        when(repository.findByTickerIgnoreCaseOrderByCreatedAtDesc("AAPL"))
                .thenReturn(List.of(sampleSignal));

        List<StockSignal> result = service.getSignalsByTicker("AAPL");

        assertThat(result).hasSize(1);
        verify(repository).findByTickerIgnoreCaseOrderByCreatedAtDesc("AAPL");
    }

    @Test
    void getSignalsByType_delegatesToRepository() {
        when(repository.findBySignalTypeOrderByCreatedAtDesc(SignalType.BUY))
                .thenReturn(List.of(sampleSignal));

        List<StockSignal> result = service.getSignalsByType(SignalType.BUY);

        assertThat(result).hasSize(1);
    }
}
