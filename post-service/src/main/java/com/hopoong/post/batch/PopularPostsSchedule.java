package com.hopoong.post.batch;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hopoong.post.api.popularpost.model.PopularPostModel;
import com.hopoong.post.api.popularpost.service.PopularPostService;
import com.hopoong.post.event.PopularPostEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PopularPostsSchedule {

    private final PopularPostService popularPostService;
    private final PopularPostEventHandler popularPostEventHandler;

    /**
     * ✅ 10분마다 인기 게시글을 조회하여 DB에 저장
     */
    @Scheduled(fixedRate = 600000) // 10분마다 실행
    public void fetchPopularPosts() {
        List<PopularPostModel.TrendingPostModel> message = popularPostService.findTop100ByOrderByViewsDesc();
        popularPostEventHandler.handleFetchPopularPostsEvent(message);
    }

    /**
     * ✅ 30분마다 Redis 캐시 갱신
     */
    @Scheduled(fixedRate = 1800000) // 30분마다 실행
    public void updatePopularPostsCache() {
        popularPostService.cachePopularPosts();
    }

}

