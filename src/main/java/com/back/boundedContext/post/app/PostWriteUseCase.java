package com.back.boundedContext.post.app;

import com.back.boundedContext.member.domain.Member;
import com.back.boundedContext.post.domain.Post;
import com.back.boundedContext.post.out.PostRepository;
import com.back.global.eventPublisher.EventPublisher;
import com.back.global.rsData.RsData;
import com.back.global.shared.post.dto.PostDto;
import com.back.global.shared.post.event.PostCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostWriteUseCase {
    private final PostRepository postRepository;
    private final EventPublisher eventPublisher;

    public RsData<Post> write(Member author, String title, String content) {
        Post post = new Post(author, title, content);
        Post savedPost = postRepository.save(post);

        eventPublisher.publish(new PostCreatedEvent(new PostDto(savedPost)));

        return new RsData<>("201-1", "%d번 글이 생성되었습니다.".formatted(post.getId()), savedPost);
    }
}
