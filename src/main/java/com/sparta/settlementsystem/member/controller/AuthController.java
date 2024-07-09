package com.sparta.settlementsystem.member.controller;


import com.sparta.settlementsystem.member.dto.LoginRequestDto;
import com.sparta.settlementsystem.member.dto.LoginResponseDto;
import com.sparta.settlementsystem.member.dto.TokenResponseDto;
import com.sparta.settlementsystem.member.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  /**
   * 로그인 요청을 처리합니다.
   *
   * @param loginRequestDto 로그인 요청 정보
   * @param response HTTP 응답 객체
   * @return 로그인 응답 DTO
   */
  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto,
                                                HttpServletResponse response) {
    return ResponseEntity.ok(authService.login(loginRequestDto, response));
  }

  /**
   * 로그아웃 요청을 처리합니다.
   * 실제 로그아웃 처리는 CustomLogoutFilter에서 수행되며,
   * 이 메서드는 추가적인 후처리가 필요한 경우에만 사용됩니다.
   *
   * @return 로그아웃 성공 시 204 No Content
   */
  @PostMapping("/logout")
  public ResponseEntity<Void> logout() {
    authService.logout();  // 추가적인 로그아웃 후처리가 필요한 경우에만 호출
    return ResponseEntity.noContent().build();
  }

  /**
   * 토큰 재발급 요청을 처리합니다.
   *
   * @param refreshToken 리프레시 토큰
   * @return 새로 발급된 토큰 응답 DTO
   */
  @PostMapping("/reissue")
  public ResponseEntity<TokenResponseDto> reissue(@CookieValue("refresh") String refreshToken,
                                                  HttpServletResponse response) {
    return ResponseEntity.ok(authService.reissue(refreshToken, response));
  }
}