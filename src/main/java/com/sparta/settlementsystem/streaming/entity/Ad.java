package com.sparta.settlementsystem.streaming.entity;

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
 * 광고(Ad) 엔티티 클래스
 * 필드 설명
 *  - id: 광고 ID (기본 키)
 *  - title: 광고 제목
 *  - url: 광고 URL
 *  - description: 광고 설명
 *  - createdAt: 광고 생성 일자
 *  - updatedAt: 광고 수정 일자
 */
@Entity
@Getter
@Table(name = "ad")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Ad {

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
  private LocalDateTime createdAt;

  @Column
  private LocalDateTime updatedAt;
}
