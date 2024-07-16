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

import java.time.LocalDateTime;

/**
 * 비디오 광고 리스트(VideoAdList) 엔티티 클래스
 * 필드 설명
 *  - id: 비디오 광고 리스트 ID (기본 키)
 *  - video: 비디오 (Video 엔티티와 다대일 관계)
 *  - ad: 광고 (Ad 엔티티와 다대일 관계)
 *  - videoAdViewCount: 비디오 광고 조회수
 *  - createdAt: 생성 일자
 *  - updatedAt: 수정 일자
 */
@Entity
@Getter
@Table(name = "video_ad_list")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class VideoAdList {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "video_id", nullable = false)
  private Video video;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ad_id", nullable = false)
  private Ad ad;

  @Column(nullable = false)
  private Long videoAdViewCount;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  private LocalDateTime updatedAt;

  /**
   * 비디오 광고 조회수를 증가시키는 메서드
   */
  public void increaseViewCount() {
    this.videoAdViewCount++;
  }
}
