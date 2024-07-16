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
 * 광고 시청 기록(AdWatchHistory) 엔티티 클래스
 * 필드 설명
 *  - id: 광고 시청 기록 ID (기본 키)
 *  - videoAdList: 광고 리스트
 *  - email: 사용자 이메일
 *  - createdAt: 시청 기록 생성 일자
 */
@Entity
@Getter
@Table(name = "ad_watch_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class AdWatchHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "ad_list_id")
  private VideoAdList videoAdList;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private Timestamp createdAt;
}
