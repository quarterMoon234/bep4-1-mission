package com.back.boundedContext.cash.app;

import com.back.boundedContext.cash.domain.CashMember;
import com.back.boundedContext.cash.domain.Wallet;
import com.back.shared.cash.dto.CashMemberDto;
import com.back.shared.market.dto.OrderDto;
import com.back.shared.market.event.MarketOrderPaymentRequestedEvent;
import com.back.shared.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CashFacade {
    private final CashCreateWalletUseCase cashCreateWalletUseCase;
    private final CashSyncMemberUseCase cashSyncMemberUseCase;
    private final CashSupport cashSupport;

    private final CashCompleteOrderPaymentUseCase cashCompleteOrderPaymentUseCase;

    @Transactional
    public CashMember syncMember(MemberDto memberDto) {
        return cashSyncMemberUseCase.syncMember(memberDto);
    }

    @Transactional
    public Wallet createWallet(CashMemberDto cashMemberDto) {
        return cashCreateWalletUseCase.createWallet(cashMemberDto);
    }

    @Transactional(readOnly = true)
    public Optional<CashMember> findCashMemberByUsername(String user) {
        return cashSupport.findCashMemberByUsername(user);
    }

    @Transactional(readOnly = true)
    public Optional<Wallet> findWalletByCashMember(CashMember cashMember) {
        return cashSupport.findWalletByCashMember(cashMember);
    }

    @Transactional
    public void completeOrderPayment(OrderDto orderDto, long pgPaymentAmount) {
        cashCompleteOrderPaymentUseCase.completeOrderPayment(orderDto, pgPaymentAmount);
    }

    @Transactional(readOnly = true)
    public Optional<Wallet> findWalletByHolderId(int holderId) {
        return cashSupport.findWalletByHolderId(holderId);
    }
}
