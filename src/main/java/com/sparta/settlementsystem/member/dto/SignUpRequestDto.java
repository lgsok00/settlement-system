package com.sparta.settlementsystem.member.dto;

import com.sparta.settlementsystem.member.entity.MemberRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequestDto {

  @NotBlank(message = "이메일은 필수 입력 항목입니다.")
  @Email(message = "유효한 이메일 형식이 아닙니다.")
  private String email;

  @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
  private String password;

  private MemberRole role;

  // toString 메서드 (비밀번호는 제외)
  @Override
  public String toString() {
    return "SignUpRequestDto{" +
            "email='" + email + '\'' +
            ", role=" + role +
            '}';
  }
}
