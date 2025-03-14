package edu.byui.apj.storefront.web.service;

import edu.byui.apj.storefront.web.model.TradingCard;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TradingCardClientService {
    private final WebClient webClient = WebClient.builder().build();

    public List<TradingCard> getAllCardsPaginated(int page, int size) {
        return (List<TradingCard>) webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("http://localhost:8080/api/cards/")
                        .queryParam("page", page)
                        .queryParam("size", size)
                        .build())
                .retrieve()
                .bodyToFlux(TradingCard.class)
                .collectList()
                .onErrorResume(e -> {
                    System.err.println("Error Fetching Data" + e.getMessage());
                    return Mono.just(Collections.emptyList());
                })
                .block();
    }

    public List<TradingCard> filterAndSort(BigDecimal minPrice, BigDecimal maxPrice, String specialty, String sort) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("http://localhost:8080/api/cards/filter")
                        .queryParamIfPresent("minPrice", Optional.ofNullable(minPrice))
                        .queryParamIfPresent("maxPrice", Optional.ofNullable(maxPrice))
                        .queryParamIfPresent("specialty", Optional.ofNullable(specialty))
                        .queryParamIfPresent("sort", Optional.ofNullable(sort))
                        .build())
                .retrieve()
                .bodyToFlux(TradingCard.class)
                .collectList()
                .onErrorResume(e -> {
                    System.err.println("Error filtering cards: " + e.getMessage());
                    return Mono.just(Collections.emptyList());
                })
                .block();
    }

    public List<TradingCard> searchByNameOrContribution(String query) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("http://localhost:8080/api/cards/search")
                        .queryParam("query", query)
                        .build())
                .retrieve()
                .bodyToFlux(TradingCard.class)
                .collectList()
                .onErrorResume(e -> {
                    System.err.println("Error searching cards: " + e.getMessage());
                    return Mono.just(Collections.emptyList());
                })
                .block();
    }
}
