package com.talenttrack.service;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class FinanceDataService {

    private final WebClient webClient;

    public FinanceDataService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://seeking-alpha.p.rapidapi.com")
                .defaultHeader("x-rapidapi-key", "cd1b36eba6msh513c010628db521p124bb3jsn3d271cd3c7e6")
                .defaultHeader("x-rapidapi-host", "seeking-alpha.p.rapidapi.com")
                .build();
    }

    public Mono<String> getRealtimeQuotes(String saIds) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/finance/realtime-quotes")
                        .queryParam("sa_ids", saIds)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }
}
