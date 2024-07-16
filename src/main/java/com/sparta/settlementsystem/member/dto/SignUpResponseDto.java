package com.sparta.settlementsystem.member.dto;

import com.sparta.settlementsystem.member.entity.Member;
import com.sparta.settlementsystem.member.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpResponseDto {
  private Long memberId;
  private String email;
  private MemberRole role;
  private Timestamp createdAt;
  private Timestamp updatedAt;

  public static SignUpResponseDto from(Member member) {
    return SignUpResponseDto.builder()
            .memberId(member.getId())
            .email(member.getEmail())
            .role(member.getRole())
            .createdAt(member.getCreatedAt())
            .updatedAt(member.getUpdatedAt())
            .build();
  }

  @Override
  public String toString() {
    return "SignUpResponseDto{" +
            "memberId=" + memberId +
            ", email='" + email + '\'' +
            ", role=" + role +
            ", createdAt=" + createdAt +
            ", updatedAt=" + updatedAt +
            '}';
  }
}
