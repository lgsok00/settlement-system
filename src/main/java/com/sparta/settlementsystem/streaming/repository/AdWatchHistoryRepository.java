package com.sparta.settlementsystem.streaming.repository;

import com.sparta.settlementsystem.streaming.entity.AdWatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * AdWatchHistory 엔티티에 대한 데이터 액세스를 제공하는 repository 입니다.
 * JpaRepository 를 상속받아 기본적인 CRUD 기능을 제공합니다.
 * 광고 시청 기록을 관리합니다.
 */
@Repository
public interface AdWatchHistoryRepository extends JpaRepository<AdWatchHistory, Long> {
  // 기본 CRUD 메서드는 JpaRepository 에서 제공됩니다.
}
