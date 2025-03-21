package com.hopoong.user.batch;

import com.hopoong.user.app.behavior.BehaviorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserActivitySchedule {


    private final BehaviorService behaviorService;

//    /*
//     * 사용자 행동 패턴 게시글 집계 처리
//     */
//    @Scheduled(fixedRate = 300000)
//    public void aggregateUserPostBehaviorMetricsSchedule() {
//        log.info("사용자 행동 패턴 게시글 집계 처리");
//        behaviorService.aggregateUserPostBehaviorMetrics();
//    }

}

