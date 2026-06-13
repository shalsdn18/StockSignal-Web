package com.stocksignal.service;

import com.stocksignal.entity.MorningBriefing;
import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import com.stocksignal.repository.MorningBriefingRepository;
import com.stocksignal.repository.StockSignalRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MorningBriefingServiceTest {

    @Mock
    private MorningBriefingRepository briefingRepository;

    @Mock
    private StockSignalRepository stockSignalRepository;

    private MorningBriefingService morningBriefingService;

    @BeforeEach
    void setUp() {
        morningBriefingService = new MorningBriefingService(briefingRepository, stockSignalRepository);
    }

    @Test
    void getLatestBriefing_returnsExistingBriefing() {
        // given
        MorningBriefing existing = new MorningBriefing("기존 브리핑", "<p>내용</p>", "중립");
        existing.setPublishedAt(LocalDateTime.now());
        given(briefingRepository.findTopByOrderByPublishedAtDesc()).willReturn(Optional.of(existing));

        // when
        MorningBriefing result = morningBriefingService.getLatestBriefing();

        // then
        assertThat(result).isEqualTo(existing);
        verify(stockSignalRepository, never()).findByDateRangeOrderByCreatedAtDesc(any(), any());
        verify(briefingRepository, never()).save(any());
    }

    @Test
    void getLatestBriefing_generatesNewBriefingWhenNoneExists() {
        // given
        given(briefingRepository.findTopByOrderByPublishedAtDesc()).willReturn(Optional.empty());
        given(stockSignalRepository.findByDateRangeOrderByCreatedAtDesc(any(), any()))
                .willReturn(Collections.emptyList());
        given(briefingRepository.save(any(MorningBriefing.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        MorningBriefing result = morningBriefingService.getLatestBriefing();

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTitle()).contains("모닝 브리핑");
        verify(briefingRepository).save(any(MorningBriefing.class));
    }

    @Test
    void generateTodayBriefing_aggregatesSignalsAndSaves() {
        // given
        LocalDate yesterday = LocalDate.now().minusDays(1);
        LocalDateTime start = yesterday.atStartOfDay();
        LocalDateTime end = yesterday.atTime(LocalTime.MAX);

        StockSignal appleBuy = new StockSignal("AAPL", SignalType.BUY, 182.50, "Golden cross");
        appleBuy.setCreatedAt(yesterday.atTime(9, 15));
        StockSignal teslaSell = new StockSignal("TSLA", SignalType.SELL, 198.40, "Resistance failed");
        teslaSell.setCreatedAt(yesterday.atTime(10, 5));
        StockSignal appleBuy2 = new StockSignal("AAPL", SignalType.BUY, 183.00, "Momentum");
        appleBuy2.setCreatedAt(yesterday.atTime(10, 45));

        List<StockSignal> signals = List.of(appleBuy, teslaSell, appleBuy2);
        given(stockSignalRepository.findByDateRangeOrderByCreatedAtDesc(start, end)).willReturn(signals);
        given(briefingRepository.save(any(MorningBriefing.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        MorningBriefing result = morningBriefingService.generateTodayBriefing();

        // then
        assertThat(result.getTitle()).contains(yesterday.toString());
        assertThat(result.getMarketStatus()).isEqualTo("강한 매수 우위");
        assertThat(result.getContent()).contains("AAPL", "TSLA", "총 신호 수: <strong>3건</strong>");
        assertThat(result.getContent()).contains("매수 신호: <strong>2건</strong>");
        assertThat(result.getContent()).contains("매도 신호: <strong>1건</strong>");

        ArgumentCaptor<MorningBriefing> captor = ArgumentCaptor.forClass(MorningBriefing.class);
        verify(briefingRepository).save(captor.capture());
        assertThat(captor.getValue().getPublishedAt()).isNotNull();
    }

    @Test
    void generateBriefingForDate_withNoSignals_returnsEmptySummary() {
        // given
        LocalDate targetDate = LocalDate.of(2026, 5, 24);
        LocalDateTime start = targetDate.atStartOfDay();
        LocalDateTime end = targetDate.atTime(LocalTime.MAX);

        given(stockSignalRepository.findByDateRangeOrderByCreatedAtDesc(start, end))
                .willReturn(Collections.emptyList());
        given(briefingRepository.save(any(MorningBriefing.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        // when
        MorningBriefing result = morningBriefingService.generateBriefingForDate(targetDate);

        // then
        assertThat(result.getContent()).contains("수집된 신호가 없습니다");
        assertThat(result.getMarketStatus()).isEqualTo("데이터 없음");
    }

    @Test
    void findBriefingForDate_returnsFirstBriefingInRange() {
        // given
        LocalDate date = LocalDate.of(2026, 5, 24);
        MorningBriefing briefing = new MorningBriefing("브리핑", "<p>내용</p>", "중립");
        briefing.setPublishedAt(date.atTime(8, 0));

        given(briefingRepository.findByPublishedAtBetweenOrderByPublishedAtDesc(
                date.atStartOfDay(), date.atTime(LocalTime.MAX)))
                .willReturn(List.of(briefing));

        // when
        Optional<MorningBriefing> result = morningBriefingService.findBriefingForDate(date);

        // then
        assertThat(result).isPresent().hasValue(briefing);
    }

    @Test
    void getAllBriefings_returnsBriefingsOrderedByPublishedAtDesc() {
        // given
        MorningBriefing oldBriefing = new MorningBriefing("Old", "<p>old</p>", "중립");
        oldBriefing.setPublishedAt(LocalDateTime.now().minusDays(2));
        MorningBriefing newBriefing = new MorningBriefing("New", "<p>new</p>", "중립");
        newBriefing.setPublishedAt(LocalDateTime.now());

        given(briefingRepository.findAllByOrderByPublishedAtDesc()).willReturn(List.of(newBriefing, oldBriefing));

        // when
        List<MorningBriefing> result = morningBriefingService.getAllBriefings();

        // then
        assertThat(result).containsExactly(newBriefing, oldBriefing);
    }
}
