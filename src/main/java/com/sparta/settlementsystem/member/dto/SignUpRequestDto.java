package com.sparta.settlementsystem.member.dto;

import com.sparta.settlementsystem.member.entity.MemberRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {

  private String email;

  private String password;

  private MemberRole role;

}
