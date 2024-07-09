package com.sparta.settlementsystem.member.repository;

import com.sparta.settlementsystem.member.entity.Member;
import com.sparta.settlementsystem.member.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

  /**
   * Refresh token 으로 RefreshToken 엔티티를 찾는다.
   *
   * @param refreshToken 찾고자 하는 Refresh token
   * @return 해당 Refresh token 을 가진 RefreshToken 엔티티 (Optional)
   */
  Optional<RefreshToken> findByRefreshToken(String refreshToken);

  /**
   * 회원의 이메일로 RefreshToken 엔티티를 찾는다.
   *
   * @param email 회원의 email
   * @return 해당 회원의 RefreshToken 엔티티 (Optional)
   */
  Optional<RefreshToken> findByMemberEmail(String email);

  /**
   * 주어진 Refresh token 으로 RefreshToken 엔티티를 삭제한다.
   * @param refreshToken 삭제하고자 하는 Refresh token
   */
  void deleteByRefreshToken(String refreshToken);

  /**
   * 주어진 Member 객체로 RefreshToken 엔티티를 삭제한다.
   * @param member 삭제하고자 하는 Refresh token 의 사용자
   */
  void deleteByMember(Member member);

  Optional<RefreshToken> findByMember(Member member);
}
