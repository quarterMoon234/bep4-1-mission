package com.back.boundedContext.market.in;

import com.back.boundedContext.market.app.MarketFacade;
import com.back.boundedContext.market.domain.Cart;
import com.back.boundedContext.market.domain.MarketMember;
import com.back.boundedContext.market.domain.Product;
import com.back.shared.post.dto.PostDto;
import com.back.shared.post.out.PostApiClient;
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
public class MarketDataInit {
    private final MarketDataInit self;
    private final MarketFacade marketFacade;
    private final PostApiClient postApiClient;

    public MarketDataInit(
            @Lazy MarketDataInit self,
            MarketFacade marketFacade,
            PostApiClient postApiClient) {
        this.self = self;
        this.marketFacade = marketFacade;
        this.postApiClient = postApiClient;
    }

    @Bean
    @Order(3)
    public ApplicationRunner marketDataInitApplicationRunner() {
        return args -> {
            self.makeBaseProducts();
            self.makeBaseCartItems();
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

        MarketMember user1MarketMember = marketFacade.findMemberByUsername("user1")
                .orElseThrow(() -> new IllegalStateException("user1 MarketMember not found"));
        MarketMember user2MarketMember = marketFacade.findMemberByUsername("user2")
                .orElseThrow(() -> new IllegalStateException("user2 MarketMember not found"));
        MarketMember user3MarketMember = marketFacade.findMemberByUsername("user3")
                .orElseThrow(() -> new IllegalStateException("user3 MarketMember not found"));

        Product product1 = marketFacade.createProduct(
                user1MarketMember,
                "Post",
                post1.getId(),
                post1.getTitle(),
                post1.getContent(),
                10_000,
                10_000
        );

        Product product2 = marketFacade.createProduct(
                user1MarketMember,
                "Post",
                post2.getId(),
                post2.getTitle(),
                post2.getContent(),
                15_000,
                15_000
        );

        Product product3 = marketFacade.createProduct(
                user1MarketMember,
                "Post",
                post3.getId(),
                post3.getTitle(),
                post3.getContent(),
                20_000,
                20_000
        );

        Product product4 = marketFacade.createProduct(
                user2MarketMember,
                "Post",
                post4.getId(),
                post4.getTitle(),
                post4.getContent(),
                25_000,
                25_000
        );

        Product product5 = marketFacade.createProduct(
                user2MarketMember,
                "Post",
                post5.getId(),
                post5.getTitle(),
                post5.getContent(),
                30_000,
                30_000
        );

        Product product6 = marketFacade.createProduct(
                user3MarketMember,
                "Post",
                post6.getId(),
                post6.getTitle(),
                post6.getContent(),
                35_000,
                35_000
        );
    }

    @Transactional
    public void makeBaseCartItems() {
        MarketMember user1Member = marketFacade.findMemberByUsername("user1")
                .orElseThrow(() -> new IllegalStateException("user1 MarketMember not found"));
        MarketMember user2Member = marketFacade.findMemberByUsername("user2")
                .orElseThrow(() -> new IllegalStateException("user2 MarketMember not found"));
        MarketMember user3Member = marketFacade.findMemberByUsername("user3")
                .orElseThrow(() -> new IllegalStateException("user3 MarketMember not found"));

        // Cart가 없으면 생성
        Cart cart1 = marketFacade.findCartByBuyer(user1Member)
                .orElseGet(() -> marketFacade.createCart(new com.back.shared.market.dto.MarketMemberDto(user1Member)).getData());
        Cart cart2 = marketFacade.findCartByBuyer(user2Member)
                .orElseGet(() -> marketFacade.createCart(new com.back.shared.market.dto.MarketMemberDto(user2Member)).getData());
        Cart cart3 = marketFacade.findCartByBuyer(user3Member)
                .orElseGet(() -> marketFacade.createCart(new com.back.shared.market.dto.MarketMemberDto(user3Member)).getData());

        Product product1 = marketFacade.findProductById(1)
                .orElseThrow(() -> new IllegalStateException("Product 1 not found"));
        Product product2 = marketFacade.findProductById(2)
                .orElseThrow(() -> new IllegalStateException("Product 2 not found"));
        Product product3 = marketFacade.findProductById(3)
                .orElseThrow(() -> new IllegalStateException("Product 3 not found"));
        Product product4 = marketFacade.findProductById(4)
                .orElseThrow(() -> new IllegalStateException("Product 4 not found"));
        Product product5 = marketFacade.findProductById(5)
                .orElseThrow(() -> new IllegalStateException("Product 5 not found"));
        Product product6 = marketFacade.findProductById(6)
                .orElseThrow(() -> new IllegalStateException("Product 6 not found"));

        if (cart1.hasItems()) return;

        cart1.addItem(product1);
        cart1.addItem(product2);
        cart1.addItem(product3);
        cart1.addItem(product4);

        cart2.addItem(product1);
        cart2.addItem(product2);
        cart2.addItem(product3);

        cart3.addItem(product1);
        cart3.addItem(product2);
    }
}