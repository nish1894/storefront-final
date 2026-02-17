package com.storefront.clients;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.storefront.dto.CheckoutRequest;
import com.storefront.dto.OrderResponse;

@Component
public class OrderClient {

    private static final Logger log = LoggerFactory.getLogger(OrderClient.class);

    private final WebClient webClient;

    public OrderClient(WebClient orderServiceWebClient) {
        this.webClient = orderServiceWebClient;
    }

    public OrderResponse checkout(CheckoutRequest request) {
        log.info("Calling order-service checkout for userId: {}", request.getUserId());

        return webClient.post()
                .uri("/api/orders/checkout")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OrderResponse.class)
                .block();
    }
}
