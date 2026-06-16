package com.stocksignal.repository;

import com.stocksignal.entity.SystemConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SystemConfigRepository extends JpaRepository<SystemConfig, Long> {
// Write-on-Read 예외를 유발하는 default getConfig() 코드를 완전히 삭제하고 빈 상태로 보존한다.
}
