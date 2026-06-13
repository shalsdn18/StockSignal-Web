package com.stocksignal.scheduler;

import com.stocksignal.entity.MorningBriefing;
import com.stocksignal.repository.MorningBriefingRepository;
import com.stocksignal.service.MorningBriefingService;
import com.stocksignal.service.TelegramNotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MorningBriefingSchedulerTest {

    @Mock
    private MorningBriefingService morningBriefingService;

    @Mock
    private MorningBriefingRepository morningBriefingRepository;

    @Mock
    private TelegramNotificationService telegramNotificationService;

    private MorningBriefingScheduler scheduler;

    @BeforeEach
    void setUp() {
        scheduler = new MorningBriefingScheduler(
                morningBriefingService,
                morningBriefingRepository,
                telegramNotificationService
        );
    }

    @Test
    void generateDailyBriefing_createsBriefingWhenNoneExists() {
        // given
        given(morningBriefingService.findBriefingForDate(LocalDate.now()))
                .willReturn(Optional.empty());

        // when
        scheduler.generateDailyBriefing();

        // then
        ArgumentCaptor<MorningBriefing> captor = ArgumentCaptor.forClass(MorningBriefing.class);
        verify(morningBriefingRepository).save(captor.capture());

        MorningBriefing saved = captor.getValue();
        assertThat(saved.getTitle()).contains("Morning Briefing");
        assertThat(saved.getContent()).contains("<h3>", "<h4>", "<p>", "<ul>", "<li>");
        assertThat(saved.getContent()).contains("나스닥 시황", "주요 거시 경제 지표");
        assertThat(saved.getPublishedAt()).isNotNull();

        verify(telegramNotificationService).sendMessage(
                "📰 [모닝 브리핑 발간] 새로운 시장 요약 리포트가 시스템에 등록되었습니다."
        );
    }

    @Test
    void generateDailyBriefing_skipsWhenBriefingAlreadyExists() {
        // given
        MorningBriefing existing = new MorningBriefing("Existing", "<p>content</p>", "중립");
        given(morningBriefingService.findBriefingForDate(LocalDate.now()))
                .willReturn(Optional.of(existing));

        // when
        scheduler.generateDailyBriefing();

        // then
        verify(morningBriefingRepository, never()).save(org.mockito.ArgumentMatchers.any());
        verify(telegramNotificationService, never()).sendMessage(org.mockito.ArgumentMatchers.any());
    }
}
