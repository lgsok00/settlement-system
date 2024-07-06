package com.sparta.settlementsystem.security.jwt;

import com.sparta.settlementsystem.member.entity.MemberRole;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

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
    return Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).getPayload().get("email", String.class);
  }

  /**
   * 토큰에서 권한을 추출하는 메서드
   * @param token JWT 토큰
   * @return 토큰에 포함된 권한
   */
  public String getRoleFromToken(String token) {
    String roleString = Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).getPayload().get("role", String.class);
    System.out.println("Role string from token: " + roleString);
    return String.valueOf(MemberRole.valueOf(roleString));
  }

  public Boolean isTokenExpired(String token) {
    return Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).getPayload().getExpiration().before(new Date());
  }

  public String createToken(String category, String email, String role, Long expiredMs) {
    return Jwts.builder()
            .claim("category", category)
            .claim("email", email)
            .claim("role", role)
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expiredMs))
            .signWith(secretKey)
            .compact();
  }

  /**
   * 토큰 판단 메서드
   * @param token
   * @return
   */
  public String getCategoryFromToken(String token) {
    return Jwts.parser().verifyWith(secretKey).build()
            .parseSignedClaims(token).getPayload().get("category", String.class);
  }
}
