package com.back.global.initData;

import com.back.boundedContext.member.app.MemberFacade;
import com.back.boundedContext.member.domain.Member;
import com.back.boundedContext.post.app.PostFacade;
import com.back.boundedContext.post.domain.Post;
import com.back.global.rsData.RsData;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration
@Slf4j
public class DataInit {
    private final DataInit self;
    private final MemberFacade memberService;
    private final PostFacade postService;

    public DataInit(
            @Lazy DataInit self,
            MemberFacade memberService,
            PostFacade postService
    ) {
        this.self = self;
        this.memberService = memberService;
        this.postService = postService;
    }

    @Bean
    public ApplicationRunner baseInitDataRunner() {
        return args -> {
            self.makeBaseMembers();
            self.makeBasePosts();
            self.makeBasePostComments();
        };
    }

    @Transactional
    public void makeBaseMembers() {
        if (memberService.count() > 0) return;

        Member systemMember = memberService.join("system", "1234", "시스템").getData();
        Member holdingMember = memberService.join("holding", "1234", "홀딩").getData();
        Member adminMember = memberService.join("admin", "1234", "관리자").getData();
        Member user1Member = memberService.join("user1", "1234", "유저1").getData();
        Member user2Member = memberService.join("user2", "1234", "유저2").getData();
        Member user3Member = memberService.join("user3", "1234", "유저3").getData();
    }

    @Transactional
    public void makeBasePosts() {
        if (postService.count() > 0) return;

        Member user1Member = memberService.findByUsername("user1").get();
        Member user2Member = memberService.findByUsername("user2").get();
        Member user3Member = memberService.findByUsername("user3").get();

        RsData<Post> post1RsData = postService.write(user1Member, "제목1", "내용1");
        log.debug(post1RsData.getMsg());

        RsData<Post> post2RsData = postService.write(user1Member, "제목2", "내용2");
        log.debug(post2RsData.getMsg());

        RsData<Post> post3RsData = postService.write(user1Member, "제목3", "내용3");
        log.debug(post3RsData.getMsg());

        RsData<Post> post4RsData = postService.write(user2Member, "제목4", "내용4");
        log.debug(post4RsData.getMsg());

        RsData<Post> post5RsData = postService.write(user2Member, "제목5", "내용5");
        log.debug(post5RsData.getMsg());

        RsData<Post> post6RsData = postService.write(user3Member, "제목6", "내용6");
        log.debug(post6RsData.getMsg());
    }

    @Transactional
    public void makeBasePostComments() {
        Post post1 = postService.findById(1).get();
        Post post2 = postService.findById(2).get();
        Post post3 = postService.findById(3).get();
        Post post4 = postService.findById(4).get();
        Post post5 = postService.findById(5).get();
        Post post6 = postService.findById(6).get();

        Member user1Member = memberService.findByUsername("user1").get();
        Member user2Member = memberService.findByUsername("user2").get();
        Member user3Member = memberService.findByUsername("user3").get();

        if (post1.hasComments()) return;

        post1.addComment(user1Member, "댓글1");
        post1.addComment(user2Member, "댓글2");
        post1.addComment(user3Member, "댓글3");

        post2.addComment(user2Member, "댓글4");
        post2.addComment(user2Member, "댓글5");

        post3.addComment(user3Member, "댓글6");
        post3.addComment(user3Member, "댓글7");

        post4.addComment(user1Member, "댓글8");
    }
}