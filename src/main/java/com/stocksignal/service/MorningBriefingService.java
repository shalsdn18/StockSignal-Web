package com.stocksignal.service;

import com.stocksignal.entity.MorningBriefing;
import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import com.stocksignal.repository.MorningBriefingRepository;
import com.stocksignal.repository.StockSignalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Business-logic service for generating and retrieving morning briefings.
 */
@Service
@Transactional
public class MorningBriefingService {

    private final MorningBriefingRepository briefingRepository;
    private final StockSignalRepository stockSignalRepository;

    public MorningBriefingService(MorningBriefingRepository briefingRepository,
                                  StockSignalRepository stockSignalRepository) {
        this.briefingRepository = briefingRepository;
        this.stockSignalRepository = stockSignalRepository;
    }

    /**
     * Returns the latest published briefing.
     * If no briefing exists, generates one for today based on yesterday's signals.
     */
    @Transactional(readOnly = true)
    public MorningBriefing getLatestBriefing() {
        return briefingRepository.findTopByOrderByPublishedAtDesc()
                .orElseGet(this::generateTodayBriefing);
    }

    /**
     * Generates a morning briefing for today using the previous trading day's signals.
     * The briefing is persisted and returned.
     */
    public MorningBriefing generateTodayBriefing() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        return generateBriefingForDate(yesterday);
    }

    /**
     * Generates a morning briefing for the given date using signals recorded
     * between 00:00 and 23:59:59 of that date.
     *
     * @param date the date to summarize
     * @return the saved briefing
     */
    public MorningBriefing generateBriefingForDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<StockSignal> signals = stockSignalRepository.findByDateRangeOrderByCreatedAtDesc(start, end);

        long buyCount = signals.stream()
                .filter(s -> s.getSignalType() == SignalType.BUY)
                .count();
        long sellCount = signals.stream()
                .filter(s -> s.getSignalType() == SignalType.SELL)
                .count();

        String title = String.format("📈 %s 모닝 브리핑", date);
        String content = buildHtmlSummary(date, signals, buyCount, sellCount);
        String marketStatus = resolveMarketStatus(buyCount, sellCount);

        MorningBriefing briefing = new MorningBriefing(title, content, marketStatus);
        if (briefing.getPublishedAt() == null) {
            briefing.setPublishedAt(LocalDateTime.now());
        }
        return briefingRepository.save(briefing);
    }

    /**
     * Finds a briefing published on the given date, if one exists.
     */
    @Transactional(readOnly = true)
    public Optional<MorningBriefing> findBriefingForDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        return briefingRepository.findByPublishedAtBetweenOrderByPublishedAtDesc(start, end)
                .stream()
                .findFirst();
    }

    /**
     * Returns all briefings ordered by publication time, newest first.
     */
    @Transactional(readOnly = true)
    public List<MorningBriefing> getAllBriefings() {
        return briefingRepository.findAllByOrderByPublishedAtDesc();
    }

    // ---- helpers ----

    private String buildHtmlSummary(LocalDate date, List<StockSignal> signals,
                                    long buyCount, long sellCount) {
        StringBuilder html = new StringBuilder();
        html.append("<p>").append(date).append(" 기준 매매 신호 요약입니다.</p>");
        html.append("<ul>");
        html.append("<li>총 신호 수: <strong>").append(signals.size()).append("건</strong></li>");
        html.append("<li>매수 신호: <strong>").append(buyCount).append("건</strong></li>");
        html.append("<li>매도 신호: <strong>").append(sellCount).append("건</strong></li>");
        html.append("</ul>");

        if (signals.isEmpty()) {
            html.append("<p>해당 일자에 수집된 신호가 없습니다.</p>");
            return html.toString();
        }

        html.append("<h4>종목별 신호 현황</h4>");
        html.append("<table class='table table-sm'>");
        html.append("<thead><tr><th>종목</th><th>유형</th><th>가격</th><th>메시지</th></tr></thead>");
        html.append("<tbody>");

        signals.stream()
                .sorted(Comparator.comparing(StockSignal::getCreatedAt,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .forEach(signal -> {
                    html.append("<tr>");
                    html.append("<td>").append(escapeHtml(signal.getTicker())).append("</td>");
                    html.append("<td>").append(signal.getSignalType()).append("</td>");
                    html.append("<td>$").append(String.format("%.2f", signal.getPrice())).append("</td>");
                    html.append("<td>").append(escapeHtml(signal.getMessage())).append("</td>");
                    html.append("</tr>");
                });

        html.append("</tbody></table>");

        Map<String, Long> topTickers = signals.stream()
                .collect(Collectors.groupingBy(StockSignal::getTicker, Collectors.counting()));

        html.append("<h4>활발 종목 TOP 3</h4>");
        html.append("<ol>");
        topTickers.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(3)
                .forEach(entry -> html.append("<li>")
                        .append(escapeHtml(entry.getKey()))
                        .append(" (").append(entry.getValue()).append("건)</li>"));
        html.append("</ol>");

        return html.toString();
    }

    private String resolveMarketStatus(long buyCount, long sellCount) {
        long total = buyCount + sellCount;
        if (total == 0) {
            return "데이터 없음";
        }
        if (buyCount > sellCount * 1.5) {
            return "강한 매수 우위";
        }
        if (buyCount > sellCount) {
            return "매수 우위";
        }
        if (sellCount > buyCount * 1.5) {
            return "강한 매도 우위";
        }
        if (sellCount > buyCount) {
            return "매도 우위";
        }
        return "중립";
    }

    private String escapeHtml(String input) {
        if (input == null) {
            return "";
        }
        return input
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}
