package com.back.boundedContext.cash.app;

import com.back.boundedContext.cash.domain.CashLog;
import com.back.boundedContext.cash.domain.Wallet;
import com.back.global.eventPublisher.EventPublisher;
import com.back.shared.cash.event.CashOrderPaymentFailedEvent;
import com.back.shared.cash.event.CashOrderPaymentSucceededEvent;
import com.back.shared.market.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CashCompleteOrderPaymentUseCase {
    private final CashSupport cashSupport;
    private final EventPublisher eventPublisher;

    public void completeOrderPayment(OrderDto orderDto, long pgPaymentAmount) {
        Wallet customerWallet = cashSupport.findWalletByHolderId(orderDto.getCustomerId()).get();
        Wallet holdingWallet = cashSupport.findHoldingWallet().get();

        // 1) PG 결제 금액이 있으면 먼저 예치금 충전
        if (pgPaymentAmount > 0) {
            customerWallet.credit(
                    pgPaymentAmount,
                    CashLog.EventType.충전__PG결제_토스페이먼츠,
                    "Order",
                    orderDto.getId()
            );
        }

        // 2) 예치금으로 주문 결제 가능한지 확인
        boolean canPay = customerWallet.getBalance() >= orderDto.getSalePrice();

        if (canPay) {
            // 고객 예치금 차감
            customerWallet.debit(
                    orderDto.getSalePrice(),
                    CashLog.EventType.사용__주문결제,
                    "Order",
                    orderDto.getId()
            );

            // holding 지갑으로 이동(임시보관)
            holdingWallet.credit(
                    orderDto.getSalePrice(),
                    CashLog.EventType.임시보관__주문결제,
                    "Order",
                    orderDto.getId()
            );

            // 성공 이벤트 발행
            eventPublisher.publish(
                    new CashOrderPaymentSucceededEvent(
                            orderDto,
                            pgPaymentAmount
                    )
            );
        } else {
            // 실패 이벤트 발행
            eventPublisher.publish(
                    new CashOrderPaymentFailedEvent(
                            "400-1",
                            "충전은 완료했지만 %번 주문을 결제완료처리를 하기에는 예치금이 부족합니다.".formatted(orderDto.getId()),
                            orderDto,
                            pgPaymentAmount,
                            pgPaymentAmount - customerWallet.getBalance()
                    )
            );
        }
    }
}

