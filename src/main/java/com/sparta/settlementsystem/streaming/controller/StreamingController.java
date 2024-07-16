package com.sparta.settlementsystem.streaming.controller;

import com.sparta.settlementsystem.security.jwt.JwtUtil;
import com.sparta.settlementsystem.streaming.dto.VideoPlayRequestDto;
import com.sparta.settlementsystem.streaming.dto.VideoPlayResponseDto;
import com.sparta.settlementsystem.streaming.dto.VideoStopRequestDto;
import com.sparta.settlementsystem.streaming.dto.VideoStopResponseDto;
import com.sparta.settlementsystem.streaming.service.StreamingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/streaming")
@RequiredArgsConstructor
public class StreamingController {

  private final StreamingService streamingService;
  private final JwtUtil jwtUtil;

  /**
   * 동영상 재생 요청을 처리합니다.
   * @param videoPlayRequestDto 동영상 재생 요청 DTO
   * @param accessToken 사용자 인증을 위한 JWT 토큰
   * @return 동영상 재생 응답 DTO
   */
  @PostMapping("/play")
  public ResponseEntity<VideoPlayResponseDto> playVideo(@RequestBody VideoPlayRequestDto videoPlayRequestDto,
                                                        @RequestHeader("Authorization") String accessToken) {

    if (accessToken != null && accessToken.startsWith("Bearer ")) {
      accessToken = accessToken.substring(7);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }


    String email = extractEmailFromToken(accessToken);
    Long videoId = videoPlayRequestDto.getVideoId();

    VideoPlayResponseDto responseDto = streamingService.playVideo(videoId, email);
    return ResponseEntity.ok(responseDto);
  }

  /**
   * 동영상 중단 요청을 처리합니다.
   * @param videoStopRequestDto 동영상 중단 요청 DTO
   * @param accessToken 사용자 인증을 위한 JWT 토큰
   * @return 동영상 중단 응답 DTO
   */
  @PostMapping("/stop")
  public ResponseEntity<VideoStopResponseDto> stopVideo(@RequestBody VideoStopRequestDto videoStopRequestDto,
                                                        @RequestHeader("Authorization") String accessToken) {

    if (accessToken != null && accessToken.startsWith("Bearer ")) {
      accessToken = accessToken.substring(7);
    } else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String email = extractEmailFromToken(accessToken);
    Long videoId = videoStopRequestDto.getVideoId();
    int stopPoint = videoStopRequestDto.getStopPoint();

    VideoStopResponseDto responseDto = streamingService.stopVideo(videoId, stopPoint, email);
    return ResponseEntity.ok(responseDto);
  }

  /**
   * JWT 토큰에서 이메일을 추출합니다.
   * @param token JWT 토큰
   * @return 이메일
   */
  private String extractEmailFromToken(String token) {
    return jwtUtil.getEmailFromToken(token);
  }
}
