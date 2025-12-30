package com.back.boundedContext.cash.app;

import com.back.boundedContext.cash.domain.CashMember;
import com.back.boundedContext.cash.domain.CashPolicy;
import com.back.boundedContext.cash.domain.Wallet;
import com.back.boundedContext.cash.out.CashMemberRepository;
import com.back.boundedContext.cash.out.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CashSupport {
    private final CashMemberRepository cashMemberRepository;
    private final WalletRepository walletRepository;

    public Optional<CashMember> findCashMemberByUsername(String user) {
        return cashMemberRepository.findByUsername(user);
    }

    public Optional<Wallet> findWalletByCashMember(CashMember cashMember) {
        return walletRepository.findByCashMember(cashMember);
    }

    public Optional<Wallet> findWalletByHolderId(int holderId) {
        return walletRepository.findByCashMemberId(holderId);
    }

    public Optional<Wallet> findHoldingWallet() {
        return walletRepository.findByCashMemberId(CashPolicy.HOLDING_MEMBER_ID);
    }

    public Wallet createWallet(CashMember cashMember) {
        Wallet wallet = new Wallet(cashMember);
        return walletRepository.save(wallet);
    }
}
