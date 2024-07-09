package com.sparta.settlementsystem.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDto {

  private String email;
  private String password;

  @Override
  public String toString() {
    return "LoginRequestDto{" +
            "email=" + email +'\'' +
            '}';
  }
}
