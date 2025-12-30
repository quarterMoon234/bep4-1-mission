package com.back.shared.member.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;

@Service
public class MemberApiClient {
    private final RestClient restClient;

    public MemberApiClient(@Value("${custom.global.internalBackUrl}") String internalBackUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(internalBackUrl + "/api/v1/members")
                .build();
    }

    public String getRandomSecureTip() {
        return restClient.get()
                .uri("/members/randomSecureTip")
                .retrieve()
                .body(String.class);
    }
}


