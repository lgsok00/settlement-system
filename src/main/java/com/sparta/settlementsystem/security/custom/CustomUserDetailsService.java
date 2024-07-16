package com.sparta.settlementsystem.security.custom;

import com.sparta.settlementsystem.member.entity.Member;
import com.sparta.settlementsystem.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  /**
   * 사용자 이메일로 UserDetails를 로드합니다.
   *
   * @param email 사용자 이메일
   * @return CustomUserDetails 객체
   * @throws UsernameNotFoundException 사용자를 찾을 수 없을 때 발생
   */
  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    log.debug("Loading UserDetails for email: {}", email);

    return memberRepository.findByEmail(email)
            .map(this::createCustomUserDetails)
            .orElseThrow(() -> {
              log.warn("User not found with email: {}", email);
              return new UsernameNotFoundException("Member not found with email: " + email);
            }
    );
  }

  /**
   * Member 엔티티로부터 CustomUserDetails 객체를 생성합니다.
   *
   * @param member Member 엔티티
   * @return CustomUserDetails 객체
   */
  private CustomUserDetails createCustomUserDetails(Member member) {
    return new CustomUserDetails(
                    member.getEmail(),
                    member.getPassword(),
                    member.getRole()
    );
  }
}
