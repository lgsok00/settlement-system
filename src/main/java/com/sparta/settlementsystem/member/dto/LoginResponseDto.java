package com.sparta.settlementsystem.member.dto;

import com.sparta.settlementsystem.member.entity.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class LoginResponseDto {

  private String email;
  private MemberRole role;
  private String accessToken;

  // toString 메서드 (토큰은 일부만 표시)
  @Override
  public String toString() {
    return "LoginResponseDto{" +
            "email='" + email + '\'' +
            ", role=" + role +
            ", accessToken='" + (accessToken != null ? accessToken.substring(0, 10) + "..." : null) + '\'' +
            '}';
  }
}
