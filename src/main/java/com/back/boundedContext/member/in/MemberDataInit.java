package com.back.boundedContext.member.in;

import com.back.boundedContext.member.app.MemberFacade;
import com.back.boundedContext.member.domain.Member;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;

@Configuration
@Slf4j
public class MemberDataInit {
    private final MemberDataInit self;
    private final MemberFacade memberService;

    public MemberDataInit(
            @Lazy MemberDataInit self,
            MemberFacade memberService
    ) {
        this.self = self;
        this.memberService = memberService;
    }

    @Bean
    @Order(1)
    public ApplicationRunner memberDataInitApplicationRunner() {
        return args -> {
            self.makeBaseMembers();
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
}
