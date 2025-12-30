package com.back.boundedContext.cash.app;

import com.back.boundedContext.cash.domain.CashLog;
import com.back.boundedContext.cash.domain.CashMember;
import com.back.boundedContext.cash.domain.Wallet;
import com.back.global.eventPublisher.EventPublisher;
import com.back.shared.cash.event.CashOrderPaymentFailedEvent;
import com.back.shared.cash.event.CashOrderPaymentSucceededEvent;
import com.back.shared.market.event.MarketOrderPaymentRequestedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashCompleteOrderPaymentUseCase {
    private final CashSupport cashSupport;
    private final EventPublisher eventPublisher;

    public void handle(MarketOrderPaymentRequestedEvent event) {
        Wallet customerWallet = cashSupport.findWalletByHolderId(event.getOrder().getCustomerId())
                .orElseThrow(() -> new IllegalStateException("Customer wallet not found for ID: " + event.getOrder().getCustomerId()));

        // Holding wallet이 없으면 생성
        Wallet holdingWallet = cashSupport.findHoldingWallet()
                .orElseGet(() -> {
                    CashMember holdingMember = cashSupport.findCashMemberByUsername("holding")
                            .orElseThrow(() -> new IllegalStateException("Holding member not found"));
                    return cashSupport.createWallet(holdingMember);
                });

        // 1) PG 결제 금액이 있으면 먼저 예치금 충전
        if (event.getPgPaymentAmount() > 0) {
            customerWallet.credit(
                    event.getPgPaymentAmount(),
                    CashLog.EventType.충전__PG결제_토스페이먼츠,
                    "Order",
                    event.getOrder().getId()
            );
        }

        // 2) 예치금으로 주문 결제 가능한지 확인
        boolean canPay = customerWallet.getBalance() >= event.getOrder().getSalePrice();

        if (canPay) {
            // 고객 예치금 차감
            customerWallet.debit(
                    event.getOrder().getSalePrice(),
                    CashLog.EventType.사용__주문결제,
                    "Order",
                    event.getOrder().getId()
            );

            // holding 지갑으로 이동(임시보관)
            holdingWallet.credit(
                    event.getOrder().getSalePrice(),
                    CashLog.EventType.임시보관__주문결제,
                    "Order",
                    event.getOrder().getId()
            );

            // 성공 이벤트 발행
            eventPublisher.publish(
                    new CashOrderPaymentSucceededEvent(
                            event.getOrder(),
                            event.getPgPaymentAmount()
                    )
            );
        } else {
            // 실패 이벤트 발행
            eventPublisher.publish(
                    new CashOrderPaymentFailedEvent(
                            "400-1",
                            "충전은 완료했지만 %번 주문을 결제완료처리를 하기에는 예치금이 부족합니다.".formatted(event.getOrder().getId()),
                            event.getOrder(),
                            event.getPgPaymentAmount(),
                            event.getPgPaymentAmount() - customerWallet.getBalance()
                    )
            );
        }
    }
}

