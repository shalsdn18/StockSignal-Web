package com.stocksignal.repository;

import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Spring Data JPA repository for {@link StockSignal}.
 */
@Repository
public interface StockSignalRepository extends JpaRepository<StockSignal, Long> {

    /** Find all signals ordered by creation time, newest first. */
    @EntityGraph(attributePaths = {"user", "memos"})
    List<StockSignal> findAllByOrderByCreatedAtDesc();

    /** Find all signals ordered by creation time, oldest first. */
    List<StockSignal> findAllByOrderByCreatedAtAsc();

    /** Find all signals for a given ticker, newest first. */
    List<StockSignal> findByTickerIgnoreCaseOrderByCreatedAtDesc(String ticker);

    /** Find all signals whose ticker contains the keyword, newest first. */
    List<StockSignal> findByTickerContainingIgnoreCaseOrderByCreatedAtDesc(String ticker);

    /** Find all signals of a given type (BUY / SELL), newest first. */
    List<StockSignal> findBySignalTypeOrderByCreatedAtDesc(SignalType signalType);

    /** Find the most recent N signals across all tickers. */
    @EntityGraph(attributePaths = {"user", "memos"})
    List<StockSignal> findTop10ByOrderByCreatedAtDesc();

    /** Find signals within a date range, newest first. */
    @Query("SELECT s FROM StockSignal s WHERE s.createdAt >= :startDateTime AND s.createdAt <= :endDateTime ORDER BY s.createdAt DESC")
    List<StockSignal> findByDateRangeOrderByCreatedAtDesc(
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    /** Find signals by ticker and within a date range, newest first. */
    @Query("SELECT s FROM StockSignal s WHERE LOWER(s.ticker) LIKE LOWER(CONCAT('%', :keyword, '%')) AND s.createdAt >= :startDateTime AND s.createdAt <= :endDateTime ORDER BY s.createdAt DESC")
    List<StockSignal> findByTickerContainingAndDateRangeOrderByCreatedAtDesc(
            @Param("keyword") String keyword,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime
    );

    /** Find signals with optional filters for ticker, date range, and signal type (AND conditions). */
    @Query("SELECT DISTINCT s FROM StockSignal s " +
            "LEFT JOIN FETCH s.user " +
            "LEFT JOIN FETCH s.memos " +
            "WHERE (:keyword IS NULL OR LOWER(s.ticker) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
            "(:startDateTime IS NULL OR s.createdAt >= :startDateTime) AND " +
            "(:endDateTime IS NULL OR s.createdAt <= :endDateTime) AND " +
            "(:signalType IS NULL OR s.signalType = :signalType) " +
            "ORDER BY s.createdAt DESC")
    List<StockSignal> findByDynamicFiltersOrderByCreatedAtDesc(
            @Param("keyword") String keyword,
            @Param("startDateTime") LocalDateTime startDateTime,
            @Param("endDateTime") LocalDateTime endDateTime,
            @Param("signalType") SignalType signalType
    );
}
