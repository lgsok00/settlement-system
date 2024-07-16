package com.sparta.settlementsystem.streaming.repository;

import com.sparta.settlementsystem.streaming.entity.Video;
import com.sparta.settlementsystem.streaming.entity.VideoAdList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * VideoAdList 엔티티에 대한 데이터 액세스를 제공하는 repository 입니다.
 * 비디오에 연결된 광고 목록을 조회하는 메서드를 포함합니다.
 */
@Repository
public interface VideoAdListRepository extends JpaRepository<VideoAdList, Long> {

  /**
   * 특정 비디오에 해당하는 모든 광고 목록을 ID 순으로 조회합니다.
   *
   * @param video 비디오
   * @return 해당 비디오에 연결된 광고 목록 (ID 순)
   */
  List<VideoAdList> findByVideoOrderById(Video video);
}
