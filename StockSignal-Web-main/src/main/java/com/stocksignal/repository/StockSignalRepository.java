package com.stocksignal.repository;

import com.stocksignal.entity.SignalType;
import com.stocksignal.entity.StockSignal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository for {@link StockSignal}.
 */
@Repository
public interface StockSignalRepository extends JpaRepository<StockSignal, Long> {

    /** Find all signals for a given ticker, newest first. */
    List<StockSignal> findByTickerIgnoreCaseOrderByCreatedAtDesc(String ticker);

    /** Find all signals of a given type (BUY / SELL), newest first. */
    List<StockSignal> findBySignalTypeOrderByCreatedAtDesc(SignalType signalType);

    /** Find the most recent N signals across all tickers. */
    List<StockSignal> findTop10ByOrderByCreatedAtDesc();
}
