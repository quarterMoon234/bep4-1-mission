package com.back.global.shared.post.event;

import com.back.global.shared.post.dto.PostCommentDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PostCommentCreatedEvent {
    private final PostCommentDto postCommentDto;
}
