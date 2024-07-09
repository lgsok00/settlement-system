package com.sparta.settlementsystem.security.config;

import com.sparta.settlementsystem.member.repository.RefreshTokenRepository;
import com.sparta.settlementsystem.security.filter.CustomLogoutFilter;
import com.sparta.settlementsystem.security.jwt.JwtFilter;
import com.sparta.settlementsystem.security.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  // AuthenticationManager 가 인자로 받을 AuthenticationConfiguration 객체 생성자 주입
  private final JwtUtil jwtUtil;
  private final RefreshTokenRepository refreshTokenRepository;

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  // password Encoder
  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // csrf disable
    http
            .csrf(AbstractHttpConfigurer::disable);

    //From 로그인 방식 disable
    http
            .formLogin(AbstractHttpConfigurer::disable);

    //http basic 인증 방식 disable
    http
            .httpBasic(AbstractHttpConfigurer::disable);

    //세션 설정
    http
            .sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

    //경로별 인가 작업
    http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/login", "/api/auth/reissue", "/api/members/signup").permitAll()
                    .requestMatchers("/seller").hasRole("SELLER")
                    .requestMatchers("/user").hasRole("USER")
                    .anyRequest().authenticated());

    // JwtFilter 등록
    http
            .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

    // CustomLogoutFilter 추가
    http
            .addFilterBefore(new CustomLogoutFilter(refreshTokenRepository), LogoutFilter.class);

    return http.build();
  }
}
