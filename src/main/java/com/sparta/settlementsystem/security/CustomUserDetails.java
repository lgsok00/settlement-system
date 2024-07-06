package com.sparta.settlementsystem.security;

import com.sparta.settlementsystem.member.dto.LoginRequestDto;
import com.sparta.settlementsystem.member.entity.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {

  private final LoginRequestDto loginRequestDto;

  public CustomUserDetails(LoginRequestDto loginRequestDto) {
    this.loginRequestDto = loginRequestDto;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    System.out.println("Role in CustomUserDetails: " + loginRequestDto.getRole());
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + loginRequestDto.getRole().name()));
  }

  @Override
  public String getPassword() {
    return loginRequestDto.getPassword();
  }

  @Override
  public String getUsername() {
    return loginRequestDto.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public MemberRole getRole() {
    return loginRequestDto.getRole();
  }
}
