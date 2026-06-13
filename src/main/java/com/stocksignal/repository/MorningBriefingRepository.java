package com.stocksignal.repository;

import com.stocksignal.entity.MorningBriefing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for {@link MorningBriefing}.
 */
@Repository
public interface MorningBriefingRepository extends JpaRepository<MorningBriefing, Long> {

    /** Find the most recently published briefing, if any. */
    Optional<MorningBriefing> findTopByOrderByPublishedAtDesc();

    /** Find all briefings ordered by publication time, newest first. */
    List<MorningBriefing> findAllByOrderByPublishedAtDesc();

    /** Find briefings published within a date range, newest first. */
    List<MorningBriefing> findByPublishedAtBetweenOrderByPublishedAtDesc(LocalDateTime start, LocalDateTime end);
}
