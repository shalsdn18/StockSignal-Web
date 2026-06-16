package com.stocksignal.scheduler;

import com.stocksignal.entity.MorningBriefing;
import com.stocksignal.repository.MorningBriefingRepository;
import com.stocksignal.service.MorningBriefingService;
import com.stocksignal.service.TelegramNotificationService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Scheduler that automatically generates the daily morning briefing.
 *
 * <p>Runs at 08:00 KST on weekdays (Mon-Fri). If a briefing already exists
 * for today, generation is skipped to avoid duplicates.</p>
 */
@Component
public class MorningBriefingScheduler {

    private static final Logger log = LoggerFactory.getLogger(MorningBriefingScheduler.class);

    private final MorningBriefingService morningBriefingService;
    private final MorningBriefingRepository morningBriefingRepository;
    private final TelegramNotificationService telegramNotificationService;

    public MorningBriefingScheduler(MorningBriefingService morningBriefingService,
                                    MorningBriefingRepository morningBriefingRepository,
                                    TelegramNotificationService telegramNotificationService) {
        this.morningBriefingService = morningBriefingService;
        this.morningBriefingRepository = morningBriefingRepository;
        this.telegramNotificationService = telegramNotificationService;
    }

    /**
     * 🤖 [시연용 오토 세이빙 아키텍처 추가]
     * 서버 구동 완료 직후 즉시 런타임에 실행되는 라이프사이클 메서드입니다.
     * 오늘자 모닝 브리핑 데이터가 데이터베이스에 비어있다면, 데모용 리포트를 즉석에서 자동 적재합니다.
     */
    @PostConstruct
    public void initDemoBriefing() {
        try {
            LocalDate today = LocalDate.now();
            boolean alreadyExists = morningBriefingService.findBriefingForDate(today).isPresent();
            
            if (!alreadyExists) {
                log.info("🤖 [시연 가이드] 오늘자 브리핑이 비어있어 데모용 리포트를 즉시 생성 보전합니다.");
                
                String title = String.format("📈 %s 주요 증시 동향 및 AI 시그널 요약 (데모 발간)", today);
                String content = buildMarketSummaryHtml(today);
                String marketStatus = "나스닥 +1.2%, 환율 1,380원 안정세";

                MorningBriefing briefing = new MorningBriefing(title, content, marketStatus);
                briefing.setPublishedAt(LocalDateTime.now());

                morningBriefingRepository.save(briefing);
                log.info("🤖 [사전 적재 완결] 시연용 데이터 준비가 성공적으로 끝났습니다.");
            }
        } catch (Exception e) {
            log.error("🚨 시연용 브리핑 데이터 적재 중 치명적 예외 격발", e);
        }
    }

    /**
     * Generates the morning briefing for today.
     * Scheduled to run at 08:00 KST on weekdays (Mon-Fri).
     */
    @Scheduled(cron = "0 0 8 * * MON-FRI", zone = "Asia/Seoul")
    public void generateDailyBriefing() {
        try {
            LocalDate today = LocalDate.now();

            boolean alreadyExists = morningBriefingService.findBriefingForDate(today).isPresent();
            if (alreadyExists) {
                log.info("Morning briefing for {} already exists. Skipping scheduled generation.", today);
                return;
            }

            log.info("Generating scheduled morning briefing for {}...", today);

            String title = String.format("📈 %s Morning Briefing", today);
            String content = buildMarketSummaryHtml(today);
            String marketStatus = "정보 수집 중";

            MorningBriefing briefing = new MorningBriefing(title, content, marketStatus);
            briefing.setPublishedAt(LocalDateTime.now());

            morningBriefingRepository.save(briefing);

            telegramNotificationService.sendMessage(
                    "📰 [모닝 브리핑 발간] 새로운 시장 요약 리포트가 시스템에 등록되었습니다."
            );

            log.info("Scheduled morning briefing for {} generated successfully.", today);
        } catch (Exception ex) {
            log.error("Failed to generate scheduled morning briefing", ex);
        }
    }

    /**
     * Builds an HTML market summary suitable for th:utext rendering.
     * Contains placeholders for Nasdaq sentiment and major macro indicators.
     */
    private String buildMarketSummaryHtml(LocalDate date) {
        return """
                <h3>%s 시장 요약</h3>
                <p>오늘 아침 주요 시황을 정리합니다.</p>

                <h4>나스닥 시황</h4>
                <p>나스닥 지수는 전장 대비 <strong>1.2%% 상승</strong> 마감했으며, 기술주 중심의 강한 매수세가 지속되고 있습니다.</p>

                <h4>주요 거시 경제 지표</h4>
                <ul>
                    <li>미국 10년물 국채 금리: <strong>4.25%%</strong></li>
                    <li>달러 인덱스(DXY): <strong>104.5</strong></li>
                    <li>원/달러 환율: <strong>1,380원</strong></li>
                    <li>WTI 유가: <strong>$78.50</strong></li>
                </ul>

                <h4>오늘의 전망</h4>
                <p>단기적으로 시장은 금리 정책과 기업 실적 발표를 주시하며 변동성을 키울 가능성이 있습니다.</p>
                """.formatted(date);
    }
}