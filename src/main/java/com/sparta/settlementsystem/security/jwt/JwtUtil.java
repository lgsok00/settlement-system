package com.sparta.settlementsystem.security.jwt;

import com.sparta.settlementsystem.member.entity.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Component
public class JwtUtil {

  private final SecretKey secretKey;

  /**
   * JwtUtil 클래스 생성자
   * @param secretKey 환경 설정에서 주입받은 비밀키
   */
  public JwtUtil(@Value("${jwt.secret}") String secretKey) {
    // 비밀키를 바이트 배열로 변환하여 HS256 알고리즘에 맞는 SecretKey 객체 생성
    this.secretKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8),
            Jwts.SIG.HS256.key().build().getAlgorithm());
  }

  /**
   * 토큰에서 이메일을 추출하는 메서드
   * @param token JWT 토큰
   * @return 토큰에 포함된 이메일
   */
  public String getEmailFromToken(String token) {
    Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    return claims.get("email", String.class);
  }

  /**
   * 토큰에서 권한을 추출하는 메서드
   * @param token JWT 토큰
   * @return 토큰에 포함된 권한 (MemberRole Enum)
   */
  public MemberRole getRoleFromToken(String token) {
    Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    String roleString = claims.get("role", String.class);
    log.debug("Role string from token: {}", roleString);

    if (roleString == null || roleString.isEmpty()) {
      throw new IllegalArgumentException("Role claim is missing or empty in the token");
    }

    try {
      return MemberRole.valueOf(roleString);
    } catch (IllegalArgumentException e) {
      log.error("Invalid role value in token: {}", roleString);
      throw new IllegalArgumentException("Invalid role in token: " + roleString, e);
    }
  }

  /**
   * 토큰의 만료 여부를 확인하는 메서드
   * @param token JWT 토큰
   * @return 토큰 만료 여부
   */
  public Boolean isTokenExpired(String token) {
    try {
      Claims claims = Jwts.parser()
              .verifyWith(secretKey)
              .build()
              .parseSignedClaims(token)
              .getPayload();
      return claims.getExpiration().before(new Date());
    } catch (ExpiredJwtException e) {
      return true;
    }
  }

  /**
   * 새로운 JWT 토큰을 생성하는 메서드
   * @param category 토큰 카테고리 (access 또는 refresh)
   * @param email 사용자 이메일
   * @param role 사용자 역할 (MemberRole Enum)
   * @param expiredMs 토큰 만료 시간 (밀리초)
   * @return 생성된 JWT 토큰
   */
  public String createToken(String category, String email, MemberRole role, Long expiredMs) {
    Date now = new Date();
    Date expiration = new Date(now.getTime() + expiredMs);

    return Jwts.builder()
            .claim("category", category)
            .claim("email", email)
            .claim("role", role.name())
            .issuedAt(now)
            .expiration(expiration)
            .signWith(secretKey)
            .compact();
  }

  /**
   * 토큰에서 카테고리를 추출하는 메서드
   * @param token JWT 토큰
   * @return 토큰에 포함된 카테고리
   */
  public String getCategoryFromToken(String token) {
    Claims claims = Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload();
    return claims.get("category", String.class);
  }

  /**
   * JWT 토큰의 유효성을 검증하는 메서드
   * @param token JWT 토큰
   * @return 토큰의 유효성 여부
   */
  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
      return true;
    } catch (SignatureException e) {
      log.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      log.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      log.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      log.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      log.error("JWT claims string is empty: {}", e.getMessage());
    }
    return false;
  }
}
