package com.hopoong.post.event;

import com.hopoong.core.model.popularpost.TrendingPostMessage;
import com.hopoong.post.adapter.kafka.KafkaProducer;
import com.hopoong.post.api.popularpost.model.PopularPostModel;
import com.hopoong.post.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PopularPostEventHandler {

    private final KafkaProducer kafkaProducer;

    /*
     * 카테고리 별 인기글 등록
     */
    public void handleFetchPopularPostsEvent(List<PopularPostModel.TrendingPostModel> data) {
        List<TrendingPostMessage> messages = MessageUtil.convertToTrendingPostMessages(data);

        // 1000건 전송
        kafkaProducer.publishPopularPostsEventIsBatch(messages);

        // 단건 전송
//        kafkaProducer.publishPopularPostsEventIsNotBatch(messages);
    }
}
