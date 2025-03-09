package com.hopoong.post.util;

import com.hopoong.core.model.popularpost.TrendingPostMessage;
import com.hopoong.post.api.popularpost.model.PopularPostModel;

import java.util.List;
import java.util.stream.Collectors;

public class MessageUtil {



    public static List<TrendingPostMessage> convertToTrendingPostMessages(List<PopularPostModel.TrendingPostModel> message) {
        return message.stream()
                .map(data ->
                        new TrendingPostMessage(data.id(), data.userId(), data.title(),
                                data.content(), data.categoryId(), data.views(), data.rn())
                )
                .collect(Collectors.toList());
    }



}
