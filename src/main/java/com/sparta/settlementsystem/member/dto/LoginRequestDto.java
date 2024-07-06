package com.sparta.settlementsystem.member.dto;

import com.sparta.settlementsystem.member.entity.MemberRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequestDto {

  private String email;
  private String password;
  private MemberRole role;

  public LoginRequestDto(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public LoginRequestDto(String email, MemberRole role) {
    this.email = email;
    this.role = role;
  }

  public LoginRequestDto(String email, String password, MemberRole role) {
    this.email = email;
    this.password = password;
    this.role = role;
  }
}
