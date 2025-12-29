package com.back.boundedContext.cash.app;

import com.back.boundedContext.cash.domain.CashMember;
import com.back.boundedContext.cash.out.CashMemberRepository;
import com.back.global.eventPublisher.EventPublisher;
import com.back.shared.cash.dto.CashMemberDto;
import com.back.shared.cash.event.CashMemberCreatedEvent;
import com.back.shared.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashSyncMemberUseCase {

    private final CashMemberRepository cashMemberRepository;
    private final EventPublisher eventPublisher;

    public CashMember syncMember(MemberDto memberDto) {

        CashMember cashMember = new CashMember(
                memberDto.getId(),
                memberDto.getCreateDate(),
                memberDto.getModifyDate(),
                memberDto.getUsername(),
                "",
                memberDto.getNickname(),
                memberDto.getActivityScore()
        );

        boolean cashMemberExists = cashMemberRepository.existsById(memberDto.getId());

        if (cashMemberExists) {
            eventPublisher.publish(
                    new CashMemberCreatedEvent(
                            new CashMemberDto(cashMember)
                    )
            );
        }

        return cashMemberRepository.save(cashMember);
    }
}
