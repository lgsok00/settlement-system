package com.sparta.settlementsystem.security.filter;

import com.sparta.settlementsystem.common.exception.CustomException;
import com.sparta.settlementsystem.member.entity.RefreshToken;
import com.sparta.settlementsystem.member.repository.RefreshTokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class CustomLogoutFilter extends OncePerRequestFilter {

  private final RefreshTokenRepository refreshTokenRepository;

  public CustomLogoutFilter(RefreshTokenRepository refreshTokenRepository) {
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    // 로그아웃 요청 URL 확인
    if ("/api/auth/logout".equals(request.getRequestURI()) && "POST".equalsIgnoreCase(request.getMethod())) {
      String refreshToken = extractRefreshTokenFromCookie(request);
      if (refreshToken != null) {
        handleLogout(refreshToken, response);
      } else {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.getWriter().write("Refresh token not found");
      }
    } else {
      filterChain.doFilter(request, response);
    }
  }

  /**
   * 로그아웃 처리를 수행하는 메서드
   */
  @Transactional
  protected void handleLogout(String refreshToken, HttpServletResponse response)
          throws IOException {
    try {
      // Refresh token 엔티티 조회
      RefreshToken refreshTokenEntity = refreshTokenRepository.findByRefreshToken(refreshToken)
              .orElseThrow(() -> new CustomException("Refresh token not found"));

      // Refresh token 삭제
      refreshTokenRepository.delete(refreshTokenEntity);

      // Refresh token 쿠키 제거
      Cookie cookie = new Cookie("refresh", null);
      cookie.setMaxAge(0);
      cookie.setPath("/");
      response.addCookie(cookie);

      response.setStatus(HttpServletResponse.SC_OK);
      response.getWriter().write("Logout successful");
      log.info("Logout successful");

    } catch (Exception e) {
      log.error("Logout failed", e);
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().write("Logout failed");
    }
  }

  /**
   * 쿠키에서 Refresh token 을 추출하는 메서드
   */
  private String extractRefreshTokenFromCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if ("refresh".equals(cookie.getName())) {
          return cookie.getValue();
        }
      }
    }
    return null;
  }
}
