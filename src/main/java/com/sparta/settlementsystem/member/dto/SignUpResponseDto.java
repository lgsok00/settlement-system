package com.sparta.settlementsystem.member.dto;

import com.sparta.settlementsystem.member.entity.Member;
import com.sparta.settlementsystem.member.entity.MemberRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class SignUpResponseDto {
  private Long memberId;
  private String email;
  private String password;
  private MemberRole role;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static SignUpResponseDto from(Member member) {
    return SignUpResponseDto.builder()
            .memberId(member.getId())
            .email(member.getEmail())
            .password(member.getPassword())
            .role(member.getRole())
            .createdAt(member.getCreatedAt())
            .updatedAt(member.getUpdatedAt())
            .build();
  }
}
