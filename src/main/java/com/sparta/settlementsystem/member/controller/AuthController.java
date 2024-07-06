package com.sparta.settlementsystem.member.controller;

import com.sparta.settlementsystem.member.entity.MemberRole;
import com.sparta.settlementsystem.member.entity.RefreshToken;
import com.sparta.settlementsystem.member.repository.RefreshTokenRepository;
import com.sparta.settlementsystem.security.jwt.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
public class AuthController {

  private final JwtUtil jwtUtil;
  private final RefreshTokenRepository refreshTokenRepository;

  @PostMapping("/reissue")
  public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response) {
    // Refresh Token 받기
    String refreshToken = null;
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {

      if (cookie.getName().equals("refresh")) {
        refreshToken = cookie.getValue();
      }
    }

    if (refreshToken == null) {
      // Response Status Body
      return new ResponseEntity<>("Refresh Token null", HttpStatus.BAD_REQUEST);
    }

    // Expired Check
    try {
      jwtUtil.isTokenExpired(refreshToken);
    } catch (ExpiredJwtException e) {
      // Response Status Code
      return new ResponseEntity<>("Refresh Token expired", HttpStatus.BAD_REQUEST);
    }

    // 토큰이 Refresh Token 인지 확인
    String category = jwtUtil.getCategoryFromToken(refreshToken);

    if (!category.equals("refresh")) {
      // Response Status Code
      return new ResponseEntity<>("Refresh Token invalid", HttpStatus.BAD_REQUEST);
    }

    // DB에 저장되어 있는지 확인
    Boolean isExist = refreshTokenRepository.existsByRefreshToken(refreshToken);
    if (!isExist) {
      // Response Body
      return new ResponseEntity<>("Refresh Token does not exist", HttpStatus.BAD_REQUEST);
    }

    String email = jwtUtil.getEmailFromToken(refreshToken);
    String role = jwtUtil.getRoleFromToken(refreshToken);

    // New JWT Token
    String newAccess = jwtUtil.createToken("access", email, role, 600000L);
    String newRefresh = jwtUtil.createToken("refresh", email, role, 86400000L);

    // Refresh Token 저장 DB 에 기존의 Refresh Token 삭제 후 새 Refresh Token 저장
    refreshTokenRepository.deleteByRefreshToken(refreshToken);
    addRefreshToken(email, newRefresh, 86400000L);


    // response
    response.setHeader("access", newAccess);
    response.addCookie(createCookie("refresh", newRefresh));

    return new ResponseEntity<>(HttpStatus.OK);
  }

  private Cookie createCookie(String key, String value) {
    Cookie cookie = new Cookie(key, value);
    cookie.setMaxAge(24 * 60 * 60);
    cookie.setHttpOnly(true);

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
}