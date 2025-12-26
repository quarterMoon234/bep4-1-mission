package com.back.boundedContext.cash.app;

import com.back.boundedContext.cash.domain.CashMember;
import com.back.boundedContext.cash.domain.Wallet;
import com.back.boundedContext.cash.out.CashMemberRepository;
import com.back.boundedContext.cash.out.WalletRepository;
import com.back.shared.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CashFacade {
    private final CashMemberRepository cashMemberRepository;
    private final WalletRepository walletRepository;

    @Transactional
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

        return cashMemberRepository.save(cashMember);
    }

    @Transactional
    public Wallet createWallet(CashMember cashMember) {
        Wallet wallet = new Wallet(cashMember);

        return walletRepository.save(wallet);
    }
}
