package com.sparta.settlementsystem.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Iterator;

@RestController
public class TestController {

  @GetMapping("/")
  public String roleEndpoint() {

    String email = SecurityContextHolder.getContext().getAuthentication().getName();

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
    Iterator<? extends GrantedAuthority> iter = authorities.iterator();
    GrantedAuthority auth = iter.next();
    String role = auth.getAuthority();

    return "email: "+ email + ", role: " + role;
  }


  @GetMapping("/seller")
  public ResponseEntity<?> sellerEndpoint() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    System.out.println("Current user authorities: " + auth.getAuthorities());
    return ResponseEntity.ok("Seller endpoint accessed successfully");
  }

  @GetMapping("/user")
  public ResponseEntity<?> userEndpoint() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    System.out.println("Current user authorities: " + auth.getAuthorities());
    return ResponseEntity.ok("User endpoint accessed successfully");
  }
}
