package com.back.shared.post.event;

import com.back.shared.post.dto.PostCommentDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PostCommentCreatedEvent {
    private final PostCommentDto postCommentDto;
}
