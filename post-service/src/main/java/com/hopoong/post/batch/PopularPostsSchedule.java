package com.hopoong.post.batch;

import com.hopoong.post.api.popularpost.service.PopularPostService;
import com.hopoong.post.api.popularpost.service.PopularPostRedisService;
import com.hopoong.post.event.PopularPostEventHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PopularPostsSchedule {

    private final PopularPostService popularPostService;
    private final PopularPostEventHandler popularPostEventHandler;
    private final PopularPostRedisService popularPostRedisService;

//    /**
//     * ✅ 10분마다 인기 게시글을 조회하여 DB에 저장
//     */
//    @Scheduled(fixedRate = 600000) // 10분마다 실행
//    public void fetchPopularPosts() {
//        List<PopularPostModel.TrendingPostModel> message = popularPostService.findTop100ByOrderByViewsDesc();
//        popularPostEventHandler.handleFetchPopularPostsEvent(message);
//    }
//
//    /**
//     * ✅ 30분마다 Redis 캐시 갱신
//     */
//    @Scheduled(fixedRate = 1800000) // 30분마다 실행
//    public void updatePopularPostsCache() {
//        popularPostService.cachePopularPosts();
//    }


//    /*
//     * 30분 마다 인기글 초기화
//     */
//    @Scheduled(fixedRate = 1800000)
//    public void resetPopularPostRanking() {
//        log.info("실시간 인기글 배치 실행");
//        popularPostRedisService.initRealTimePopularPostCount();
//    }



}

