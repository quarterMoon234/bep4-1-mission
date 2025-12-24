package com.back.shared.post.event;

import com.back.shared.post.dto.PostDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class PostCreatedEvent {
    private final PostDto postDto;
}
