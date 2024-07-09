package com.sparta.settlementsystem.member.service;

import com.sparta.settlementsystem.common.exception.CustomException;
import com.sparta.settlementsystem.member.dto.LoginRequestDto;
import com.sparta.settlementsystem.member.dto.LoginResponseDto;
import com.sparta.settlementsystem.member.dto.TokenResponseDto;
import com.sparta.settlementsystem.member.entity.Member;
import com.sparta.settlementsystem.member.entity.MemberRole;
import com.sparta.settlementsystem.member.entity.RefreshToken;
import com.sparta.settlementsystem.member.repository.MemberRepository;
import com.sparta.settlementsystem.member.repository.RefreshTokenRepository;
import com.sparta.settlementsystem.security.jwt.JwtUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

  private final MemberRepository memberRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  @Value("${jwt.expiration.access}")
  private Long accessTokenExpired;

  @Value("${jwt.expiration.refresh}")
  private Long refreshTokenExpired;

  /**
   * 로그인 처리
   * @param loginRequestDto 로그인 요청 DTO
   * @param response HTTP 응답 객체
   * @return 로그인 응답 DTO
   */
  @Transactional
  public LoginResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
    // 이메일로 회원 조회
    Member member = memberRepository.findByEmail(loginRequestDto.getEmail())
            .orElseThrow(() -> new CustomException("Member not found"));

    // 비밀번호 검증
    if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
      throw new CustomException("Invalid password");
    }

    // Access token, Refresh token 생성
    String accessToken = jwtUtil.createToken("access", member.getEmail(),
            member.getRole(), accessTokenExpired);
    String refreshToken = jwtUtil.createToken("refresh", member.getEmail(),
            member.getRole(), refreshTokenExpired);

    // Refresh token DB 저장
    saveRefreshToken(member, refreshToken);

    // Refresh token 쿠키 저장
    Cookie refreshTokenCookie = createRefreshTokenCookie(refreshToken);

    // Response header, Cookie 설정
    response.addHeader("Authorization", "Bearer " + accessToken);
    response.addCookie(refreshTokenCookie);

    return new LoginResponseDto(member.getEmail(), member.getRole(), accessToken);
  }

  /**
   * 로그아웃 후처리
   * 이 메서드는 CustomLogoutFilter 에서 주요 로그아웃 로직이 처리된 후 호출됩니다.
   * 추가적인 로그아웃 관련 작업이 필요한 경우 여기에서 처리합니다.
   */
  @Transactional
  public void logout() {
    log.info("Additional logout processing in AuthService if needed");
  }

  /**
   * 토큰 재발급 처리
   * @param oldRefreshToken 리프레시 토큰
   * @return 새로 발급된 토큰 응답 DTO
   */
  @Transactional
  public TokenResponseDto reissue(String oldRefreshToken, HttpServletResponse response) {
    // Refresh token 만료 여부 확인
    if (jwtUtil.isTokenExpired(oldRefreshToken)) {
      throw new CustomException("Refresh token expired");
    }

    // Refresh token 에서 email, role 추출
    String email = jwtUtil.getEmailFromToken(oldRefreshToken);
    MemberRole role = jwtUtil.getRoleFromToken(oldRefreshToken);

    // 사용자 조회
    Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException("Member not found"));

    // 기존 Refresh token 조회
    RefreshToken refreshToken = refreshTokenRepository.findByMemberEmail(email)
            .orElseThrow(() -> new CustomException("Refresh token not found"));

    // 저장된 Refresh token 과 요청으로 받은 Refresh token 비교
    if(!refreshToken.getRefreshToken().equals(oldRefreshToken)) {
      throw new CustomException("Refresh token mismatch");
    }

    // 새로운 Access token, Refresh token 생성
    String newAccessToken = jwtUtil.createToken("access", email, role, accessTokenExpired);
    String newRefreshToken = jwtUtil.createToken("refresh", email, role, refreshTokenExpired);

    // Refresh token 업데이트
    refreshToken.updateToken(newRefreshToken, LocalDateTime.now().plusSeconds(refreshTokenExpired / 1000));
    refreshTokenRepository.save(refreshToken);

    // Access Token 응답 헤더 추가
    response.addHeader("Authorization", "Bearer " + newAccessToken);

    Cookie refreshTokenCookie = createRefreshTokenCookie(newRefreshToken);
    response.addCookie(refreshTokenCookie);

    return new TokenResponseDto(newAccessToken, newRefreshToken);
  }

  /**
   * Refresh token 저장
   * @param member 회원 엔티티
   * @param refreshToken 리프레시 토큰
   */
  private void saveRefreshToken(Member member, String refreshToken) {
    RefreshToken token = RefreshToken.builder()
            .member(member)
            .refreshToken(refreshToken)
            .expiredAt(LocalDateTime.now().plusSeconds(refreshTokenExpired / 1000))
            .build();
    refreshTokenRepository.save(token);
  }

  /**
   * Refresh token 쿠키 생성
   * @param refreshToken 리프레시 토큰
   * @return 생성된 쿠키
   */
  private Cookie createRefreshTokenCookie(String refreshToken) {
    Cookie cookie = new Cookie("refresh", refreshToken);
    cookie.setMaxAge((int) (refreshTokenExpired / 1000));
    cookie.setHttpOnly(true);

    return cookie;
  }
}
