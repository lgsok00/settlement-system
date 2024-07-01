package com.sparta.settlementsystem.user.dto;

import com.sparta.settlementsystem.user.entity.User;
import com.sparta.settlementsystem.common.type.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpRequestDto {
  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String password;

  @NotBlank
  private UserRole role;

  public User toEntity() {
    return User.builder()
            .email(email)
            .password(password)
            .role(role)
            .build();
  }
}
