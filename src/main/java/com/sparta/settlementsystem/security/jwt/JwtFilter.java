package com.sparta.settlementsystem.security.jwt;

import com.sparta.settlementsystem.member.dto.LoginRequestDto;
import com.sparta.settlementsystem.member.entity.MemberRole;
import com.sparta.settlementsystem.security.CustomUserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

public class JwtFilter extends OncePerRequestFilter {

  private final JwtUtil jwtUtil;

  public JwtFilter(JwtUtil jwtUtil) {
    this.jwtUtil = jwtUtil;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
    // 헤더에서 access 키에 담긴 토큰 꺼냄
    String authorizationHeader = request.getHeader("Authorization");

    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    // "Bearer " 이후의 토큰 문자열 추출
    String accessToken = authorizationHeader.substring(7);

    // 토큰 만료 여부 확인, 만료시 다음 필터로 넘기지 않음
    try {
      jwtUtil.isTokenExpired(accessToken);

    } catch (ExpiredJwtException e) {

      // Response body
      PrintWriter writer = response.getWriter();
      writer.print("Access Token expired");

      // Response Status Code
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // 토큰이 access 인지 확인
    String category = jwtUtil.getCategoryFromToken(accessToken);

    if (!category.equals("access")) {
      // Response body
      PrintWriter writer = response.getWriter();
      writer.print("invalid access token");

      // Response Status Code
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      return;
    }

    // email, role 값
    String email = jwtUtil.getEmailFromToken(accessToken);
    String role = jwtUtil.getRoleFromToken(accessToken);

    LoginRequestDto loginRequestDto = new LoginRequestDto(email, role);
    CustomUserDetails customUserDetails = new CustomUserDetails(loginRequestDto);

    Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authToken);

    filterChain.doFilter(request, response);
  }
}

