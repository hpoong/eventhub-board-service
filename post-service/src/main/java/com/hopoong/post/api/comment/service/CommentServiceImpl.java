package com.hopoong.post.api.comment.service;

import com.hopoong.post.event.CommentEventHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentEventHandler commentEventHandler;

    @Override
    public void test(String message) {
        commentEventHandler.handleCommentEvent(message);
    }
}
