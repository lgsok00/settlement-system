package com.sparta.settlementsystem.streaming.service;

import com.sparta.settlementsystem.streaming.dto.VideoPlayResponseDto;
import com.sparta.settlementsystem.streaming.dto.VideoStopResponseDto;
import com.sparta.settlementsystem.streaming.entity.AdWatchHistory;
import com.sparta.settlementsystem.streaming.entity.Video;
import com.sparta.settlementsystem.streaming.entity.VideoAdList;
import com.sparta.settlementsystem.streaming.entity.VideoWatchHistory;
import com.sparta.settlementsystem.streaming.repository.AdWatchHistoryRepository;
import com.sparta.settlementsystem.streaming.repository.VideoAdListRepository;
import com.sparta.settlementsystem.streaming.repository.VideoRepository;
import com.sparta.settlementsystem.streaming.repository.VideoWatchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class StreamingService {

  private final VideoRepository videoRepository;
  private final VideoAdListRepository videoAdListRepository;
  private final VideoWatchHistoryRepository videoWatchHistoryRepository;
  private final AdWatchHistoryRepository adWatchHistoryRepository;

  /**
   * 동영상 재생 API
   *
   * <p>주어진 동영상 ID와 사용자 이메일을 기반으로 동영상을 재생합니다.
   * 해당 동영상의 조회수를 증가시키고, 사용자의 시청 기록을 관리하여
   * 재생 위치를 결정합니다. 최종적으로 동영상 재생 정보를 응답 DTO로 반환합니다.</p>
   *
   * @param videoId 재생할 동영상 ID
   * @param email   사용자 이메일 (JWT 토큰에서 추출)
   * @return 동영상 재생 응답 DTO
   * @throws IllegalArgumentException 동영상이 존재하지 않을 경우 발생
   */
  public VideoPlayResponseDto playVideo(Long videoId, String email) {
    // videoId 에 해당하는 동영상을 조회하고, 없으면 예외를 던짐
    Video video = videoRepository.findById(videoId)
            .orElseThrow(() -> new IllegalArgumentException
                    ("해당 동영상이 존재하지 않습니다. videoId = " + videoId));

    // 동영상 조회수 증가
    video.increaseViewCount();

    // 사용자 이메일과 동영상으로 가장 최근의 시청 기록 조회
    Optional<VideoWatchHistory> optionalVideoWatchHistory = videoWatchHistoryRepository
            .findTopByEmailAndVideoOrderByCreatedAtDesc(email, video);

    VideoWatchHistory videoWatchHistory;

    if (optionalVideoWatchHistory.isPresent()) {
      // 시청 기록이 존재하면 마지막 시청 기록을 가져옴
      VideoWatchHistory lastHistory = optionalVideoWatchHistory.get();
      if (lastHistory.getPlaybackTimeline() == video.getRunningTime()) {
        // 마지막 시청 기록이 동영상의 전체 재생 시간을 완료했을 경우, 처음부터 재생
        videoWatchHistory = VideoWatchHistory.builder()
                .video(video)
                .email(email)
                .playbackTimeline(0)
                .createdAt(Timestamp.from(Instant.now()))
                .build();
      } else {
        // 그렇지 않으면 마지막 시청 위치에서 재생
        videoWatchHistory = VideoWatchHistory.builder()
                .video(video)
                .email(email)
                .playbackTimeline(lastHistory.getPlaybackTimeline())
                .createdAt(Timestamp.from(Instant.now()))
                .build();
      }
    } else {
      // 시청 기록이 존재하지 않으면 처음부터 재생
      videoWatchHistory = VideoWatchHistory.builder()
              .video(video)
              .email(email)
              .playbackTimeline(0)
              .createdAt(Timestamp.from(Instant.now()))
              .build();
    }

    // 시청 기록 저장
    videoWatchHistoryRepository.save(videoWatchHistory);

    // 동영상 재생 응답 DTO 생성 및 반환
    return VideoPlayResponseDto.from(videoWatchHistory);
  }

  /**
   * 동영상 중단 API
   *
   * <p>주어진 동영상 ID와 사용자 이메일을 기반으로 동영상 재생을 중단합니다.
   * 중단 시점을 저장하고, 시청 기록을 업데이트하며, 필요한 경우 광고 시청 기록도 관리합니다.
   * 최종적으로 동영상 중단 정보를 응답 DTO로 반환합니다.</p>
   *
   * @param videoId   중단할 동영상 ID
   * @param stopPoint 중단 시점 (초 단위)
   * @param email     사용자 이메일 (JWT 토큰에서 추출)
   * @return 동영상 중단 응답 DTO
   * @throws IllegalArgumentException 동영상이나 시청 기록이 존재하지 않을 경우, 중단 시점이 이전 재생 시점보다 작을 경우 발생
   */

  public VideoStopResponseDto stopVideo(Long videoId, int stopPoint, String email) {
    // videoId에 해당하는 동영상을 조회하고, 없으면 예외를 던짐
    Video video = videoRepository.findById(videoId)
            .orElseThrow(() -> new IllegalArgumentException
                    ("해당 동영상이 존재하지 않습니다. videoId = " + videoId));

    // 사용자 이메일과 동영상으로 가장 최근의 시청 기록 조회
    VideoWatchHistory lastHistory = videoWatchHistoryRepository
            .findTopByEmailAndVideoOrderByCreatedAtDesc(email, video)
            .orElseThrow(() -> new IllegalArgumentException("해당 동영상의 시청 기록이 존재하지 않습니다."));

    // 중단 시점이 이전 재생 시점보다 작을 경우 예외를 던짐
    if (lastHistory.getPlaybackTimeline() > stopPoint) {
      throw new IllegalArgumentException("중단 시점이 이전 재생 시점보다 작습니다.");
    }

    // 새로운 동영상 시청 기록을 생성
    VideoWatchHistory videoWatchHistory = VideoWatchHistory.builder()
            .video(video)
            .email(email)
            .playbackTimeline(stopPoint)
            .createdAt(Timestamp.from(Instant.now()))
            .build();

    // 동영상 길이가 300초 이상인 경우 광고 시청 기록을 관리
    if (video.getRunningTime() > 300) {
      List<VideoAdList> adLists = videoAdListRepository.findByVideoOrderById(video);

      int maxAdCount = video.getRunningTime() / 300;
      int adCount = 0;

      for (int i = 300; i <= stopPoint && adCount < maxAdCount; i += 300) {
        if (adCount < adLists.size()) {
          VideoAdList adList = adLists.get(adCount);

          int adStartTime = (adCount + 1) * 300;

          // 마지막 시청 시점 이후에 중단되었는지 확인
          if (lastHistory.getPlaybackTimeline() < adStartTime && stopPoint >= adStartTime) {
            adList.increaseViewCount();

            AdWatchHistory adWatchHistory = AdWatchHistory.builder()
                    .videoAdList(adList)
                    .email(email)
                    .createdAt(Timestamp.from(Instant.now()))
                    .build();

            adWatchHistoryRepository.save(adWatchHistory);
          }

          adCount++;
        }
      }
    }

    // 중단 시점이 동영상 전체 재생 시간을 초과한 경우 재생 시점을 동영상 길이로 설정
    if (stopPoint >= video.getRunningTime()) {
      videoWatchHistory.updatePlaybackTimeline(video.getRunningTime());
    }

    // 동영상 시청 기록 저장
    videoWatchHistoryRepository.save(videoWatchHistory);

    // 동영상 중단 응답 DTO 생성 및 반환
    return VideoStopResponseDto.from(videoWatchHistory);
  }
}
