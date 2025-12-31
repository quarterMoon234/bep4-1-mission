package com.back.shared.post.dto;

import com.back.boundedContext.post.domain.Post;
import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;


@AllArgsConstructor
@Getter
public class PostDto {
    private final int id;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifyDate;
    private final int authorId;
    private final String authorName;
    private final String title;
    private final String content;
}
