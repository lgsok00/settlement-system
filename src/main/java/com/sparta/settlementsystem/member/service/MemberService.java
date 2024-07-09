package com.sparta.settlementsystem.member.service;

import com.sparta.settlementsystem.common.exception.CustomException;
import com.sparta.settlementsystem.member.dto.SignUpRequestDto;
import com.sparta.settlementsystem.member.dto.SignUpResponseDto;
import com.sparta.settlementsystem.member.entity.Member;
import com.sparta.settlementsystem.member.entity.MemberRole;
import com.sparta.settlementsystem.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final PasswordEncoder passwordEncoder;

  // 회원가입
  public SignUpResponseDto signUp(SignUpRequestDto signUpRequestDto) {
    // 이메일 중복 검증
    if (memberRepository.existsByEmail(signUpRequestDto.getEmail())) {
      throw new CustomException("Email already exists.");
    }

    Member member = Member.createMember(
            signUpRequestDto.getEmail(),
            passwordEncoder.encode(signUpRequestDto.getPassword()),
            signUpRequestDto.getRole()
    );

    Member savedMember = memberRepository.save(member);

    return SignUpResponseDto.from(savedMember);
  }
}
