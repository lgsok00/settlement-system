package com.sparta.settlementsystem.security;

import com.sparta.settlementsystem.member.dto.LoginRequestDto;
import com.sparta.settlementsystem.member.entity.Member;
import com.sparta.settlementsystem.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final MemberRepository memberRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

    Member member = memberRepository.findByEmail(email)
            .orElseThrow(() ->
               new UsernameNotFoundException("Member not found with email: " + email));

    // Member 엔티티에서 LoginRequestDto 로 필요한 정보 반환
    LoginRequestDto loginRequestDto = new LoginRequestDto(
            member.getEmail(),
            member.getPassword(),
            member.getRole()
    );

    return new CustomUserDetails(loginRequestDto);
  }
}
