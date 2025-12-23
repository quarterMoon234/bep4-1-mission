package com.back.global.shared.post.event;

import com.back.global.shared.post.dto.PostDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PostCreatedEvent {
    private final PostDto postDto;
}
