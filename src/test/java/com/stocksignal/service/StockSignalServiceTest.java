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
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        verify(telegramService).sendMessage(contains("Ticker: AAPL"));
        verify(telegramService).sendMessage(contains("Type: BUY"));
        verify(telegramService).sendMessage(contains("Price: $182.50"));
        verify(telegramService).sendMessage(contains("Message: Test signal"));
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
    void getAllSignals_usesRepositoryOrderedQuery() {
        when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(sampleSignal));

        List<StockSignal> result = service.getAllSignals();

        assertThat(result).hasSize(1);
        verify(repository).findAllByOrderByCreatedAtDesc();
    }

    @Test
    void getAllSignals_returnsListSortedNewestFirst() {
        StockSignal older = new StockSignal("GOOG", SignalType.BUY, 100.0, null);
        older.setCreatedAt(LocalDateTime.now().minusDays(1));
        StockSignal newer = new StockSignal("MSFT", SignalType.SELL, 200.0, null);
        newer.setCreatedAt(LocalDateTime.now());

        when(repository.findAllByOrderByCreatedAtDesc()).thenReturn(List.of(newer, older));

        List<StockSignal> result = service.getAllSignals();

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getTicker()).isEqualTo("MSFT");
        verify(repository).findAllByOrderByCreatedAtDesc();
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
    void searchSignalsByTicker_usesContainsIgnoreCaseQuery() {
        when(repository.findByTickerContainingIgnoreCaseOrderByCreatedAtDesc("AAP"))
                .thenReturn(List.of(sampleSignal));

        List<StockSignal> result = service.searchSignalsByTicker("AAP");

        assertThat(result).hasSize(1);
        verify(repository).findByTickerContainingIgnoreCaseOrderByCreatedAtDesc("AAP");
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

    @Test
    void searchSignalsByDateRange_usesDatabaseQuery() {
        java.time.LocalDate today = java.time.LocalDate.now();
        when(repository.findByDateRangeOrderByCreatedAtDesc(
                org.mockito.ArgumentMatchers.argThat(dt -> dt.toLocalDate().equals(today)),
                org.mockito.ArgumentMatchers.any(java.time.LocalDateTime.class)
        )).thenReturn(List.of(sampleSignal));

        List<StockSignal> result = service.searchSignalsByDateRange(today, today);

        assertThat(result).hasSize(1);
    }

    @Test
    void searchSignalsByDateRange_swapsWhenEndDateIsBeforeStartDate() {
        java.time.LocalDate start = java.time.LocalDate.of(2026, 6, 13);
        java.time.LocalDate end = java.time.LocalDate.of(2026, 6, 10);

        when(repository.findByDateRangeOrderByCreatedAtDesc(
                org.mockito.ArgumentMatchers.argThat(dt -> dt.toLocalDate().equals(end)),
                org.mockito.ArgumentMatchers.any(java.time.LocalDateTime.class)
        )).thenReturn(List.of());

        service.searchSignalsByDateRange(start, end);

        // Verify that the swapped (normalized) dates were used
        verify(repository).findByDateRangeOrderByCreatedAtDesc(
                org.mockito.ArgumentMatchers.argThat(dt -> dt.toLocalDate().equals(end)),
                org.mockito.ArgumentMatchers.any(java.time.LocalDateTime.class)
        );
    }

    @Test
    void searchSignalsByTickerAndDateRange_combinedFiltering() {
        java.time.LocalDate today = java.time.LocalDate.now();
        when(repository.findByTickerContainingAndDateRangeOrderByCreatedAtDesc(
                org.mockito.ArgumentMatchers.eq("AAP"),
                org.mockito.ArgumentMatchers.argThat(dt -> dt.toLocalDate().equals(today)),
                org.mockito.ArgumentMatchers.any(java.time.LocalDateTime.class)
        )).thenReturn(List.of(sampleSignal));

        List<StockSignal> result = service.searchSignalsByTickerAndDateRange("AAP", today, today);

        assertThat(result).hasSize(1);
    }

    @Test
    void searchSignalsByDynamicFilters_withSignalTypeOnly() {
        when(repository.findByDynamicFiltersOrderByCreatedAtDesc(
                null,
                null,
                null,
                SignalType.BUY
        )).thenReturn(List.of(sampleSignal));

        List<StockSignal> result = service.searchSignalsByDynamicFilters(null, null, null, SignalType.BUY);

        assertThat(result).hasSize(1);
    }

    @Test
    void searchSignalsByDynamicFilters_withAllFilters() {
        java.time.LocalDate today = java.time.LocalDate.now();
        when(repository.findByDynamicFiltersOrderByCreatedAtDesc(
                org.mockito.ArgumentMatchers.eq("AAP"),
                org.mockito.ArgumentMatchers.argThat(dt -> dt.toLocalDate().equals(today)),
                org.mockito.ArgumentMatchers.any(java.time.LocalDateTime.class),
                org.mockito.ArgumentMatchers.eq(SignalType.BUY)
        )).thenReturn(List.of(sampleSignal));

        List<StockSignal> result = service.searchSignalsByDynamicFilters("AAP", today, today, SignalType.BUY);

        assertThat(result).hasSize(1);
    }

    @Test
    void searchSignalsByDynamicFilters_withNoFilters() {
        when(repository.findByDynamicFiltersOrderByCreatedAtDesc(
                null,
                null,
                null,
                null
        )).thenReturn(List.of(sampleSignal));

        List<StockSignal> result = service.searchSignalsByDynamicFilters(null, null, null, null);

        assertThat(result).hasSize(1);
    }
}
