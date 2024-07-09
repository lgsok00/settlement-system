package com.sparta.settlementsystem.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponseDto {
  private String accessToken;
  private String refreshToken;

  // toString 메서드 (토큰은 일부만 표시)
  @Override
  public String toString() {
    return "TokenResponseDto{" +
            "accessToken='" + (accessToken != null ? accessToken.substring(0, 10) + "..." : null) + '\'' +
            ", refreshToken='" + (refreshToken != null ? refreshToken.substring(0, 10) + "..." : null) + '\'' +
            '}';
  }
}
