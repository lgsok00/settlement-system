package com.sparta.settlementsystem.security.filter;

import com.sparta.settlementsystem.member.repository.RefreshTokenRepository;
import com.sparta.settlementsystem.security.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

public class CustomLogoutFilter extends GenericFilterBean {

  private final JwtUtil jwtUtil;
  private final RefreshTokenRepository refreshTokenRepository;

  public CustomLogoutFilter(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
    this.jwtUtil = jwtUtil;
    this.refreshTokenRepository = refreshTokenRepository;
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
          throws IOException, ServletException {

    doFilter((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse, filterChain);
  }

  private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws IOException, ServletException {
    logger.info("CustomLogoutFilter: Starting logout process");

    // Path and Method Verify
    String requestUri = request.getRequestURI();
    logger.info("CustomLogoutFilter: RequestURI: " + requestUri);
    if (!requestUri.matches("^\\/logout$")) {
      logger.info("CustomLogoutFilter: Not a logout request, proceeding with filter chain");
      filterChain.doFilter(request, response);
      return;
    }

    String requestMethod = request.getMethod();
    logger.info("CustomLogoutFilter: Request method: " + requestMethod);
    if (!requestMethod.equals("POST")) {
      logger.info("CustomLogoutFilter: Not a POST request, proceeding with filter chain");
      filterChain.doFilter(request, response);
      return;
    }

    // Get Refresh Token
    String refreshToken = null;
    Cookie[] cookies = request.getCookies();
    logger.info("CustomLogoutFilter: Checking Cookies");

    if (cookies != null) {
      for (Cookie cookie : cookies) {
        if (cookie.getName().equals("refresh")) {
          refreshToken = cookie.getValue();
          logger.info("CustomLogoutFilter: Refresh Token found in cookies");
          break;
        }
      }
    } else {
      logger.warn("CustomLogoutFilter: No cookies found in the request");
    }

    // Refresh Token null check
    if (refreshToken == null) {
      logger.error("CustomLogoutFilter: Refresh token is null");
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    // Expired Check
    try {
      jwtUtil.isTokenExpired(refreshToken);
      logger.info("CustomLogoutFilter: Refresh token is not expired");
    } catch (ExpiredJwtException e) {
      logger.error("CustomLogoutFilter: Refresh token has expired", e);
      // Response Status Code
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    // Token 이 Refresh Token 인지 확인
    String category = jwtUtil.getCategoryFromToken(refreshToken);
    logger.info("CustomLogoutFilter: Category: " + category);
    if (!category.equals("refresh")) {
      logger.error("CustomLogoutFilter: Token is not a refresh token");
      // Response Status Code
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    // DB에 저장되어 있는지 확인
    Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
    logger.info("CustomLogoutFilter: Refresh token exists in DB: " + isExist);
    if (!isExist) {
      logger.info("CustomLogoutFilter: Refresh token does not exist in DB");
      // Response Status Code
      response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    // 로그아웃 진행
    // RefreshToken DB 제거
    refreshTokenRepository.deleteByRefreshToken(refreshToken);
    logger.info("CustomLogoutFilter: Refresh token deleted from DB");

    // RefreshToken Cookie 값 0
    Cookie refreshTokenCookie = new Cookie("refresh", null);
    refreshTokenCookie.setMaxAge(0);
    refreshTokenCookie.setPath("/");

    response.addCookie(refreshTokenCookie);
    logger.info("CustomLogoutFilter: Refresh token cookie invalidated");
    response.setStatus(HttpServletResponse.SC_OK);
    logger.info("CustomLogoutFilter: Logout successful");
  }
}
