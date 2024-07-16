package com.sparta.settlementsystem.streaming.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

/**
 * 비디오 시청 기록(VideoWatchHistory) 엔티티 클래스
 * 필드 설명
 *  - id: 비디오 시청 기록 ID (기본 키)
 *  - video: 비디오 (Video 엔티티와 다대일 관계)
 *  - email: 사용자 이메일
 *  - playbackTimeline: 재생 위치 (초 단위)
 *  - createdAt: 시청 기록 생성 일자
 */
@Entity
@Getter
@Table(name = "video_watch_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VideoWatchHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "video_id", nullable = false)
  private Video video;

  @Column(nullable = false)
  private String email;

  private int playbackTimeline;

  @Column(nullable = false)
  private Timestamp createdAt;

  public void updatePlaybackTimeline(int playbackTimeline) {
    this.playbackTimeline = playbackTimeline;
  }
}
