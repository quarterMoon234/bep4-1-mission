package com.back.shared.market.out;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TossSecretKeyTest {

    @Value("${custom.market.toss.payments.secretKey:}")
    private String tossSecretKey;

    @Test
    void testSecretKeyIsLoaded() {
        System.out.println("=== 시크릿 키 로드 확인 ===");

        // 시크릿 키가 로드되었는지 확인
        assertThat(tossSecretKey)
            .as("시크릿 키가 비어있지 않아야 함")
            .isNotEmpty();

        // 시크릿 키가 올바른 형식인지 확인
        assertThat(tossSecretKey)
            .as("시크릿 키는 test_sk_ 또는 live_sk_로 시작해야 함")
            .matches("(test_sk_|live_sk_).*");

        // 시크릿 키 앞 20자만 출력 (보안)
        String maskedKey = tossSecretKey.substring(0, Math.min(20, tossSecretKey.length())) + "***";
        System.out.println("로드된 시크릿 키: " + maskedKey);
        System.out.println("시크릿 키 길이: " + tossSecretKey.length());

        System.out.println("\n✅ 시크릿 키가 정상적으로 로드되었습니다!");
    }
}