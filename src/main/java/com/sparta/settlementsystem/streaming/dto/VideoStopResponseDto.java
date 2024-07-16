package com.sparta.settlementsystem.streaming.dto;

import com.sparta.settlementsystem.streaming.entity.VideoWatchHistory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoStopResponseDto {

  private String email;
  private Long videoId;
  private int playbackTimeline;
  private Timestamp createdAt;

  public static VideoStopResponseDto from(VideoWatchHistory videoWatchHistory) {
    return VideoStopResponseDto.builder()
            .email(videoWatchHistory.getEmail())
            .videoId(videoWatchHistory.getVideo().getId())
            .playbackTimeline(videoWatchHistory.getPlaybackTimeline())
            .createdAt(videoWatchHistory.getCreatedAt())
            .build();
  }
}
