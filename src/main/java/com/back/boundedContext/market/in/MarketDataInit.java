package com.back.boundedContext.market.in;

import com.back.boundedContext.market.app.MarketFacade;
import com.back.boundedContext.market.domain.MarketMember;
import com.back.shared.post.dto.PostDto;
import com.back.shared.post.out.PostApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class MarketDataInit {
    private final PostApiClient postApiClient;
    private final MarketFacade marketFacade;

    @Bean
    @Order(3)
    public ApplicationRunner marketDataInitApplicationRunner() {
        return args -> {
            makeBaseProducts();
        };
    }

    @Transactional
    public void makeBaseProducts() {
        if (marketFacade.productCount() > 0) return;

        List<PostDto> posts = postApiClient.getItems();

        PostDto post1 = posts.get(5);
        PostDto post2 = posts.get(4);
        PostDto post3 = posts.get(3);
        PostDto post4 = posts.get(2);
        PostDto post5 = posts.get(1);
        PostDto post6 = posts.get(0);

        MarketMember user1 = marketFacade.findMemberByUsername("user1").get();
        MarketMember user2 = marketFacade.findMemberByUsername("user2").get();
        MarketMember user3 = marketFacade.findMemberByUsername("user3").get();

        marketFacade.createProduct(user1, "Post", post1.getId(), post1.getTitle(), post1.getContent(), 10_000, 10_000);
        marketFacade.createProduct(user1, "Post", post2.getId(), post2.getTitle(), post2.getContent(), 15_000, 15_000);
        marketFacade.createProduct(user1, "Post", post3.getId(), post3.getTitle(), post3.getContent(), 20_000, 20_000);

        marketFacade.createProduct(user2, "Post", post4.getId(), post4.getTitle(), post4.getContent(), 25_000, 25_000);
        marketFacade.createProduct(user2, "Post", post5.getId(), post5.getTitle(), post5.getContent(), 30_000, 30_000);

        marketFacade.createProduct(user3, "Post", post6.getId(), post6.getTitle(), post6.getContent(), 35_000, 35_000);

    }
}
