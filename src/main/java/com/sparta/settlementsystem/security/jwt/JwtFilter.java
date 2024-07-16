package com.sparta.settlementsystem.security.jwt;

import com.sparta.settlementsystem.member.entity.MemberRole;
import com.sparta.settlementsystem.security.custom.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {

    // 헤더에서 Authorization 토큰 추출
    String authorizationHeader = request.getHeader("Authorization");

    // 로그인 요청이거나 헤더가 비어있는 경우 필터 통과
    if (isPublicEndpoint(request) || !isValidAuthorizationHeader(authorizationHeader)) {
      filterChain.doFilter(request, response);
      return;
    }

    // "Bearer " 이후의 토큰 문자열 추출
    String accessToken = authorizationHeader.substring(7);

    // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
    try {
      if (jwtUtil.isTokenExpired(accessToken)) {
        handleExpiredToken(response);
        return;
      }

      if (!isAccessToken(accessToken)) {
        handleInvalidAccessToken(response);
        return;
      }

      // 토큰에서 사용자 정보 추출 및 인증 객체 생성
      authenticateUser(accessToken);

      filterChain.doFilter(request, response);

    } catch (Exception e) {
      log.error("Error processing JWT token", e);
      handleTokenProcessingError(response);
    }

  }

  /**
   * 공개 엔드포인트 여부 확인
   */
  private boolean isPublicEndpoint(HttpServletRequest request) {
    return request.getRequestURI().endsWith("/api/auth/login");
  }

  /**
   * Authorization 헤더 유효성 검사
   */
  private boolean isValidAuthorizationHeader(String authorizationHeader) {
    return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
  }

  /**
   * 만료된 액세스 토큰 처리
   */
  private void handleExpiredToken(HttpServletResponse response) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write("Access Token expired");
  }

  /**
   * 유효하지 않은 액세스 토큰 처리
   */
  private void handleInvalidAccessToken(HttpServletResponse response) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write("Invalid access token");
  }

  /**
   * 토큰 처리 중 발생한 예외 처리
   */
  private void handleTokenProcessingError(HttpServletResponse response) throws IOException {
    response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    response.getWriter().write("Error processing token");
  }

  /**
   * 액세스 토큰 여부 확인
   */
  private boolean isAccessToken(String token) {
    return jwtUtil.getCategoryFromToken(token).equals("access");
  }

  /**
   * 사용자 인증 처리
   */
  private void authenticateUser(String accessToken) {
    String email = jwtUtil.getEmailFromToken(accessToken);
    MemberRole role = jwtUtil.getRoleFromToken(accessToken);

    CustomUserDetails customUserDetails = new CustomUserDetails(email, null, role);
    Authentication authToken = new UsernamePasswordAuthenticationToken
            (customUserDetails, null, customUserDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authToken);
  }
}

