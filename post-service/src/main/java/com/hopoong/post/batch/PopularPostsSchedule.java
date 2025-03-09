package com.hopoong.post.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hopoong.post.api.popularpost.model.PopularPostModel;
import com.hopoong.post.api.popularpost.service.PopularPostService;
import com.hopoong.post.api.popularpost.service.PopularPostRedisService;
import com.hopoong.post.event.PopularPostEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PopularPostsSchedule {

    private final PopularPostService popularPostService;
    private final PopularPostEventHandler popularPostEventHandler;
    private final PopularPostRedisService popularPostRedisService;

//    /*
//     * 인기 게시글을 조회하여 DB에 저장
//     */
//    @Scheduled(fixedRate = 600000) // 10분마다 실행
//    public void fetchPopularPosts() {
//        log.info("인기 게시글을 조회하여 DB에 저장");
//        List<PopularPostModel.TrendingPostModel> message = popularPostService.findTop100ByOrderByViewsDesc();
//        popularPostEventHandler.handleFetchPopularPostsEvent(message);
//    }

//    /*
//     * 인기 게시글을 조회하여 Redis 캐시 갱신
//     */
//    @Scheduled(fixedRate = 300000) // 30분마다 실행
//    public void updatePopularPostsCache() throws JsonProcessingException {
//        log.info("인기 게시글을 조회하여 Redis 캐시 갱신");
//        popularPostService.saveTrendingPostsByCategory();
//    }
//
//    /*
//     * 실시간 인기글 초기화
//     */
//    @Scheduled(fixedRate = 1800000) // 30분마다 실행
//    public void resetPopularPostRankingSchedule() {
//        log.info("실시간 인기글 초기화");
//        popularPostRedisService.initRealTimePopularPostCount();
//    }
//
//    /*
//     * 사용자 행동 패턴 게시글 집계 처리
//     */
//    @Scheduled(fixedRate = 300000) // 5분마다 실행
//    public void aggregatePostMetricsSchedule() {
//        log.info("사용자 행동 패턴 게시글 집계 처리");
//        popularPostService.aggregatePostMetrics();
//    }
}

