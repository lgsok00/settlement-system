package com.sparta.settlementsystem.user.dto;

import com.sparta.settlementsystem.user.entity.User;
import com.sparta.settlementsystem.common.type.UserRole;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
public class SignUpResponseDto {
  private Long id;
  private String email;
  private String password;
  private UserRole role;
  private Timestamp createdAt;
  private Timestamp updatedAt;

  public SignUpResponseDto(User user) {
    this.id = user.getId();
    this.email = user.getEmail();
    this.password = user.getPassword();
    this.role = user.getRole();
    this.createdAt = user.getCreatedAt();
    this.updatedAt = user.getUpdatedAt();
  }
}
