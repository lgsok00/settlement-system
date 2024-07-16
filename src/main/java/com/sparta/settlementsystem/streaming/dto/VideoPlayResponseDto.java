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
public class VideoPlayResponseDto {

  private String email;
  private Long videoId;
  private int playbackTimeline;
  private Long videoViewCount;
  private Timestamp createdAt;

  public static VideoPlayResponseDto from(VideoWatchHistory videoWatchHistory) {
    return VideoPlayResponseDto.builder()
            .email(videoWatchHistory.getEmail())
            .videoId(videoWatchHistory.getVideo().getId())
            .playbackTimeline(videoWatchHistory.getPlaybackTimeline())
            .videoViewCount(videoWatchHistory.getVideo().getVideoViewCount())
            .createdAt(videoWatchHistory.getCreatedAt())
            .build();
  }
}
