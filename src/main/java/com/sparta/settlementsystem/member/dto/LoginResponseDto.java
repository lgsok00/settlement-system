package com.sparta.settlementsystem.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

  private String email;
  private String accessToken;
  private String refreshToken;
}
