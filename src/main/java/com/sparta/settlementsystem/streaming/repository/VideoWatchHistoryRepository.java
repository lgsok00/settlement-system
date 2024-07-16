package com.sparta.settlementsystem.streaming.repository;

import com.sparta.settlementsystem.streaming.entity.Video;
import com.sparta.settlementsystem.streaming.entity.VideoWatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * VideoWatchHistory 엔티티에 대한 데이터 액세스를 제공하는 repository 입니다.
 * 사용자의 비디오 시청 기록을 관리하는 메서드를 포함합니다.
 */
@Repository
public interface VideoWatchHistoryRepository extends JpaRepository<VideoWatchHistory, Long> {

  /**
   * 특정 사용자의 특정 비디오 시청 기록을 시청 시간 내림차순으로 조회합니다.
   *
   * @param email 사용자의 이메일
   * @param video 비디오
   * @return 가장 최근의 비디오 시청 기록의 Optional 객체
   */
  Optional<VideoWatchHistory> findTopByEmailAndVideoOrderByCreatedAtDesc(String email, Video video);
}
