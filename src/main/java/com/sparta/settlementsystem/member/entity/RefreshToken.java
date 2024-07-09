package com.sparta.settlementsystem.member.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Getter
@Table(name = "refresh_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RefreshToken {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // Member 와 RefreshToken 간 1:1 관계
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id", unique = true)
  private Member member;

  @Column(nullable = false)
  private String refreshToken;

  @Column(nullable = false)
  private LocalDateTime expiredAt;

  @Builder
  public RefreshToken(Member member, String refreshToken, LocalDateTime expiredAt) {
    this.member = member;
    this.refreshToken = refreshToken;
    this.expiredAt = expiredAt;
  }

  public void updateToken(String refreshToken, LocalDateTime expiredAt) {
    this.refreshToken = refreshToken;
    this.expiredAt = expiredAt;
  }
}
