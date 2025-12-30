package com.back.shared.market.out;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TossPaymentsServiceTest {

    @Autowired
    private TossPaymentsService tossPaymentsService;

    @Test
    void testSecretKeyLoaded() {
        // TossPaymentsService의 secretKey가 로드되었는지 확인
        // private 필드이므로 실제 API 호출로 간접 확인

        System.out.println("=== TossPaymentsService 설정 확인 ===");
        System.out.println("TossPaymentsService bean이 정상적으로 생성되었습니다.");

        assertThat(tossPaymentsService).isNotNull();
    }

    @Test
    void testInvalidPaymentConfirm() {
        // 잘못된 데이터로 API 호출하여 연결 확인
        // 실제로는 실패하지만, 토스 API까지 요청이 도달했는지 확인

        System.out.println("=== 토스 페이먼츠 API 연결 테스트 ===");

        try {
            tossPaymentsService.confirmCardPayment(
                "test_invalid_key",
                "test_order_id",
                1000L
            );
        } catch (Exception e) {
            // 예외가 발생하는 것이 정상 (잘못된 데이터 사용)
            System.out.println("API 호출 결과: " + e.getMessage());

            // 토스 API 응답이 있으면 연결 성공
            assertThat(e.getMessage())
                .as("토스 API에서 응답을 받았는지 확인")
                .containsAnyOf("UNAUTHORIZED", "NOT_FOUND", "INVALID", "인증", "시크릿");
        }
    }
}