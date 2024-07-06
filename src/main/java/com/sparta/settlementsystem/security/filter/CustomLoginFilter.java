package com.sparta.settlementsystem.security.filter;

import com.sparta.settlementsystem.member.entity.MemberRole;
import com.sparta.settlementsystem.member.entity.RefreshToken;
import com.sparta.settlementsystem.member.repository.RefreshTokenRepository;
import com.sparta.settlementsystem.security.CustomUserDetails;
import com.sparta.settlementsystem.security.jwt.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Iterator;

public class CustomLoginFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final RefreshTokenRepository refreshTokenRepository;

  public CustomLoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
    this.authenticationManager = authenticationManager;
    this.jwtUtil = jwtUtil;
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
    // 클라이언트 요청에서 email, password 추출
    String email = obtainEmail(request);
    String password = obtainPassword(request);

    // Spring Security 에서 email, password 검증을 위한 token 생성
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password, null);

    // token 검증을 위해 AuthenticationManager 전달
    return this.authenticationManager.authenticate(authToken);
  }


  // email 을 얻기 위한 메서드 오버라이드
  @Override
  protected String obtainUsername(HttpServletRequest request) {
    return request.getParameter("email");
  }

  // obtainEmail 메서드 추가
  protected String obtainEmail(HttpServletRequest request) {
    return obtainUsername(request);
  }

  // 로그인 성공 시 호출 (JWT 발급)
  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
    CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

    String email = userDetails.getUsername();

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
    GrantedAuthority auth = iterator.next();

    String role = auth.getAuthority();
    // Token 생성
    String accessToken = jwtUtil.createToken("access", email, role, 600000L);
    String refreshToken = jwtUtil.createToken("refresh", email, role, 86400000L);

    // Refresh Token 저장
    addRefreshToken(email, refreshToken, 86400000L);
    logger.info("Refresh token saved for user: " + email);

    // Access Token 응답
    response.addHeader("Authorization", "Bearer " + accessToken);

    // Refresh Token 응답
    Cookie refreshTokenCookie = createCookie("refresh", refreshToken);
    response.addCookie(refreshTokenCookie);
    //response.addCookie(createCookie("refresh", refreshToken));
    response.setStatus(HttpStatus.OK.value());
  }

  /**
   * Cookie 생성 메서드
   * @param key
   * @param value
   * @return
   */
  private Cookie createCookie(String key, String value) {
    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(24 * 60 * 60);
    // cookie.setSecure(true);  // HTTPS 통신 시 설정
    // cookie.setPath("/");  // Cookie 가 적용될 범위 설정
    cookie.setHttpOnly(true);  // 클라이언트에서 접근 하지 못하도록 설정

    return cookie;
  }

  private void addRefreshToken(String email, String refreshToken, Long expiredMs) {
    LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(expiredMs / 1000);

    RefreshToken refresh = new RefreshToken();
    refresh.setEmail(email);
    refresh.setRefreshToken(refreshToken);
    refresh.setExpiredAt(expiredAt);

    refreshTokenRepository.save(refresh);
  }

  // 로그인 실패 시 호출
  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

    // 로그인 실패시 401 응답 코드 반환
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
  }
}
