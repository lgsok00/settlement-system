package com.sparta.settlementsystem.streaming.entity;

import com.sparta.settlementsystem.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 비디오(Video) 엔티티 클래스
 * 필드 설명
 *  - id: 비디오 ID (기본 키)
 *  - title: 비디오 제목
 *  - url: 비디오 URL
 *  - description: 비디오 설명
 *  - runningTime: 비디오 재생 시간 (초 단위)
 *  - videoViewCount: 비디오 조회수
 *  - nextCalcDate: 다음 계산 일자
 */
@Entity
@Table(name = "video")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Video extends BaseTimeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String url;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private int runningTime;

  private Long videoViewCount;

  private LocalDateTime nextCalcDate;

  /**
   * 비디오 조회수를 증가시키는 메서드
   */
  public void increaseViewCount() {
    this.videoViewCount++;
  }
}
