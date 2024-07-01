package com.sparta.settlementsystem.user.service;

import com.sparta.settlementsystem.user.dto.SignUpRequestDto;
import com.sparta.settlementsystem.user.dto.SignUpResponseDto;
import com.sparta.settlementsystem.user.entity.User;
import com.sparta.settlementsystem.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  // 회원가입
  public SignUpResponseDto signUpUser(SignUpRequestDto signUpRequestDto) {
    User user = signUpRequestDto.toEntity();
    User signUpUser = userRepository.save(user);
    return new SignUpResponseDto(signUpUser);
  }
}
