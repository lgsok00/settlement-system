package com.sparta.settlementsystem.user.controller;

import com.sparta.settlementsystem.user.dto.SignUpRequestDto;
import com.sparta.settlementsystem.user.dto.SignUpResponseDto;
import com.sparta.settlementsystem.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/signup")
  public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto signUpRequestDto) {
    SignUpResponseDto signUpUser = userService.signUpUser(signUpRequestDto);
    return ResponseEntity.ok(signUpUser);
  }


}
