package com.sparta.settlementsystem.member.controller;

import com.sparta.settlementsystem.member.dto.SignUpRequestDto;
import com.sparta.settlementsystem.member.dto.SignUpResponseDto;
import com.sparta.settlementsystem.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  // 회원가입
  @PostMapping("/signup")
  public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto requestDto) {
    SignUpResponseDto responseDto = memberService.signUp(requestDto);
    return ResponseEntity.ok(responseDto);
  }
}
