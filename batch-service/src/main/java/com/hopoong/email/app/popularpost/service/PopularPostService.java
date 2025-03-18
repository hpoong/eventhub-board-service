package com.hopoong.email.app.popularpost.service;

import com.hopoong.core.model.popularpost.TrendingPostMessage;
import com.hopoong.email.app.popularpost.repository.PopularPostJdbcRepository;
import com.hopoong.email.domain.PopularPostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopularPostService {

    private final PopularPostJdbcRepository popularPostJdbcRepository;

    @Transactional
    public void savePopularPost(List<TrendingPostMessage> allTrendingPosts) {
        List<PopularPostEntity> popularPostEntities = allTrendingPosts.stream()
                .map(data -> new PopularPostEntity(null, data.id(), data.views(), Math.toIntExact(data.rn()), data.retrievedAt(), LocalDateTime.now()))
                .collect(Collectors.toList());

        popularPostJdbcRepository.batchInsert(popularPostEntities);
    }




}
