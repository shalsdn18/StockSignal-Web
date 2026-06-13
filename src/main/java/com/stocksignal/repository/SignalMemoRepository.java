package com.stocksignal.repository;

import com.stocksignal.entity.SignalMemo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignalMemoRepository extends JpaRepository<SignalMemo, Long> {
}